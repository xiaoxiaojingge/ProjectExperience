package com.itjing.community.response;

/**
 * @author: lijing
 * @Date: 2021年07月31日 17:52
 * @Description: 通用响应格式数据
 */
public class CommonResult<T> {

    /**
     * 状态码: (如果状态码多,可以用枚举)
     * 200代表响应成功
     * 500代表响应失败
     */
    private int code;

    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;

    /**
     * 响应的具体数据
     */
    private T data;

    /**
     * 分页数据
     */
//    private PagePaging page;

    /**
     * @param code       状态码
     * @param msg        响应信息，用来说明响应情况
     * @param data       响应的具体数据
     * @param pageNum    当前页码
     * @param pageSize   每页数量
     * @param pages      页码总数
     * @param total      记录总数
     */
   /* private CommonResult(int code, String msg, T data, int pageNum, int pageSize, int pages, long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        page=new PagePaging();
        this.page.setPageNum(pageNum);
        this.page.setPageSize(pageSize);
        this.page.setPages(pages);
        this.page.setTotal(total);
    }*/

    private CommonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    /**
     * 成功返回(用于单条)
     * @param msg   响应信息，用来说明响应情况
     * @param data  响应数据
     * @return
     */
    public static <T> CommonResult<T> resultSuccess(String msg, T data) {
        return new CommonResult(200, msg, data);
    }

    /**
     * 成功返回(用于数量多的数据)
     * @param msg   响应信息，用来说明响应情况
     * @param data  响应数据
     * @return
     */
    /*public static <T> CommonResult<T> resultSuccessList(String msg, List<T> data) {
        PageInfo<?> pf = new PageInfo<>(data);
        return new CommonResult(200, msg, data, pf.getPageNum(),pf.getPageSize(),pf.getPages(), pf.getTotal());
    }*/

    /**
     * 失败返回
     * @param msg   响应信息，用来说明响应情况
     * @return
     */
    public static <T> CommonResult<T> resultError(String msg) {
        return new CommonResult(500, msg);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
