package com.itjing.community.controller;

import com.itjing.community.annotation.LoginRequired;
import com.itjing.community.constant.CommunityConstant;
import com.itjing.community.entity.Comment;
import com.itjing.community.entity.DiscussPost;
import com.itjing.community.entity.Page;
import com.itjing.community.entity.User;
import com.itjing.community.service.*;
import com.itjing.community.utils.CommunityUtil;
import com.itjing.community.utils.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lijing
 * @Date: 2021年07月31日 13:49
 * @Description:
 */
@Controller
@RequestMapping("user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    /**
     * 账号设置页
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        // 上传文件名称
        String fileName = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);

        return "/site/setting";
    }

    /**
     * 更新头像路径
     *
     * @param fileName
     * @return
     */
    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "文件名不能为空!");
        }

        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

        return CommunityUtil.getJSONString(0);
    }


    /**
     * 上传头像
     * 废弃，使用云服务器，直接上传到云服务器
     *
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @Deprecated
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8888/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 读取头像
     * 废弃，使用云服务器，直接从云服务器读取
     *
     * @param fileName
     * @param response
     */
    @Deprecated
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/changePassword", method = {RequestMethod.GET, RequestMethod.POST})
    public String changePassword(String oldPassword, String newPassword, Model model) {

        User user = hostHolder.getUser();

        Map<String, Object> map = userService.changePassword(user, oldPassword, newPassword);

        if (map == null || map.isEmpty()) {
            return "redirect:/index";
        } else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }

    /**
     * 个人主页
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {

        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);

        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

    /**
     * 我的帖子
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path = "/myPost/{userId}", method = RequestMethod.GET)
    public String getMyPostList(@PathVariable("userId") int userId, Page page, Model model) {

        int discussPostsRows = discussPostService.findDiscussPostRows(userId);

        page.setLimit(5);

        page.setPath("/user/myPost/" + userId);

        List<DiscussPost> list = discussPostService.findDiscussPosts(userId, page.getOffset(), page.getLimit(), 0);

        List<Map<String, Object>> discussPosts = new ArrayList<>();

        if (list != null) {
            for (DiscussPost post : list) {

                Map<String, Object> map = new HashMap<>(8);

                map.put("post", post);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());

                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }

        model.addAttribute("discussPostsRows", discussPostsRows);

        model.addAttribute("discussPosts", discussPosts);

        model.addAttribute("userId", userId);

        return "/site/my-post";
    }

    /**
     * 我的帖子
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path = "/myReply/{userId}", method = RequestMethod.GET)
    public String getMyReplyList(@PathVariable("userId") int userId, Page page, Model model) {

        int commentRows = commentService.findCommentRows(userId, ENTITY_TYPE_POST);

        page.setLimit(5);

        page.setRows(commentRows);

        page.setPath("/user/myReply/" + userId);

        List<Comment> list = commentService.findComments(userId, page.getOffset(), page.getLimit(), ENTITY_TYPE_POST);

        List<Map<String, Object>> comments = new ArrayList<>();

        if (list != null) {
            for (Comment comment : list) {

                Map<String, Object> map = new HashMap<>(8);

                map.put("comment", comment);

                DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());

                map.put("post", post);

                comments.add(map);
            }
        }

        model.addAttribute("commentRows", commentRows);

        model.addAttribute("comments", comments);

        model.addAttribute("userId", userId);

        return "/site/my-reply";
    }
}
