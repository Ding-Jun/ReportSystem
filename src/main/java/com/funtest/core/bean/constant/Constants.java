package com.funtest.core.bean.constant;

/**
 * 
 * 类Constants.java的实现描述：TODO 类实现描述 
 * @author Administrator 2015年12月10日 下午2:49:15
 */
public class Constants {
	public static final Integer CHART_PASS=0;
	public static final Integer CHART_FAIL=-1;
	public static final Integer CHART_ALL=1;
	public static final Integer CHART_GROUPS_PASS_DEFAULT=100;
	public static final Integer CHART_GRUOPS_FAIL_DEFAULT=20;
    public static final String FILE_UPLOAD_DIR="upload";
	
	public static final Integer PROCESS_MODE_NORMAL=0;
	public static final Integer PROCESS_MODE_DELETE_FT_FAIL=1;
	public static final Integer PROCESS_STATUS_ERROR_FORMAT=1001;
	public static final Integer PROCESS_STATUS_CANT_FIND_TESTITEM=1002;
	public static final Integer PROCESS_STATUS_CANT_FIND_LIMIT=1003;
	public static final Integer PROCESS_STATUS_TESTITEM_NOT_MATCH=1004;
	public static final Integer PROCESS_STATUS_LIMIT_NOT_MATCH=1005;
	public static final Integer PROCESS_STATUS_UNIT_NOT_MATCH=1006;
	public static final Integer PROCESS_STATUS_INITIAL_STATE=1007;
	public static final Integer PROCESS_STATUS_HEAD_DONE_WITH_UNIT=1008;
	public static final Integer PROCESS_STATUS_HEAD_DONE_WITHOUT_UNIT=1009;
	public static final Integer PROCESS_STATUS_DONE=1111;
	
    public static final String SESSION_USER_KEY = "curUser";
    public static final String SESSION_USER_NAME_KEY = "curUserId";
    public static final Object SESSION_FORCED_LOGOUT_KEY = "forcedLogout";

    // RETURN MESSAGE
    public static final Integer RETURN_MSG_INIT = 0;
    public static final Integer RETURN_MSG_FAILURE = -1;
    public static final Integer RETURN_MSG_SUCCESS = 1;

    public static final Object UNDERLINE = "_";
    public static final String UNICODE_UTF8 = "utf8";

    //    public static final int PAGE_SIZE_DEFAULT = 10;
    public static final int PAGE_SIZE_DEFAULT = 20;
    public static final int PAGE_CUR_PAGE_DEFAULT = 1;
    public static final int PAGE_START_PAGE_INDEX = 1;

    public static final String PAGE_ORDER_DEFAULT = "DESC";

    //COMMENT STATUS
    public static final Integer COMMENT_ALL=0;//ALL
    public static final Integer COMMENT_BEFORE_AUDIT=1;//未审核
    public static final Integer COMMENT_AFTER_AUDIT_PASS=2;//通过审核
    public static final Integer COMMENT_AFTER_AUDIT_FAIL=3;//未通过审核
    
    public static final String RE_LOGIN = "reLogin";
    public static final String SYS_ERROR = "系统异常";
    public static final String LOGIN_FAILED = "验证失败";

    public static final String ANONYMOUS_USER = "匿名用户";

    public static final long SESSION_DEFAULT_TIMEOUT = 30;

    public static final int RESPONSE_STATUS_NOT_FOUND = 403;

    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_1 = "商品名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_2 = "商业体名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_3 = "店铺名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_4 = "商品编码";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_5 = "原价";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_6 = "售价";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_7 = "商品描述";

    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_1_ORDER_ERROR = "文件第1列必须为商品名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_2_ORDER_ERROR = "文件第2列必须为商业体名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_3_ORDER_ERROR = "文件第3列必须为店铺名称";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_4_ORDER_ERROR = "文件第4列必须为商品编码";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_5_ORDER_ERROR = "文件第5列必须为原价";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_6_ORDER_ERROR = "文件第6列必须为售价";
    public static final String UPLOAD_EXCEL_ITEM_SHEET_TITLE_7_ORDER_ERROR = "文件第7列必须为商品描述";
    public static final Integer UPLOAD_EXCEL_ITEM_CELL_SIZE = 7;

    public static final String UPLOAD_EXCEL_ITEM_CELL_SIZE_ERROR = "格式错误,请严格按照模板规范上传文件";

    public static final int UPLOAD_EXCEL_ITEM_BATCH_OBJ_SIZE = 6;

    public static final Integer UPLOAD_EXCEL_ITEM_DEFAULT_STATUS = 0;
    public static final Integer ITEM_DEFAULT_STATUS = 0;
    public static final Integer ITEM_NOT_AUDIT_STATUS = 0;
    public static final Integer ITEM_SHELVE_STATUS = 1;
    public static final Integer ITEM_UNSHELVE_STATUS = 2;
    //    public static final Integer ITEM_SUSPEND_STATUS = 2;
    public static final Integer ITEM_SOFT_DELETE_STATUS = 4;
    public static final Integer ITEM_AUDIT_NOT_PASS_STATUS = 9;

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    public static final Integer ITEM_DEFAULT_PRIORITY = 999;

    public static final Integer COMBO_HEAD_VALUE_ITEM_TYPE = -1;
    public static final String COMBO_HEAD_LABEL_ITEM_TYPE = "选择商品分类";
    public static final Integer COMBO_HEAD_VALUE_MALL = -1;
    public static final String COMBO_HEAD_LABEL_MALL = "选择商业体";
    public static final Integer COMBO_HEAD_VALUE_PROVINCE = -1;
    public static final String COMBO_HEAD_LABEL_PROVINCE = "选择省";
    public static final Integer COMBO_HEAD_VALUE_CITY = -1;
    public static final String COMBO_HEAD_LABEL_CITY = "选择市";
    public static final Integer COMBO_HEAD_VALUE_MALL_TYPE = -1;
    public static final String COMBO_HEAD_LABEL_MALL_TYPE = "选择商业体分类";
    public static final Integer COMBO_HEAD_VALUE_SHOP = -1;
    public static final String COMBO_HEAD_LABEL_SHOP = "选择店铺";
    public static final Integer COMBO_HEAD_VALUE_SHOP_TYPE = -1;
    public static final String COMBO_HEAD_LABEL_SHOP_TYPE = "选择店铺分类";
    public static final Integer COMBO_HEAD_VALUE_MALL_ACT = -1;
    public static final String COMBO_HEAD_LABEL_MALL_ACT = "选择优惠活动";

    public static final Integer UPLOAD_IMG_LENGTH_ERROR = -2;
    public static final Integer UPLOAD_IMG_SIZE_ERROR = -3;

    public static final Integer ADMIN_USER_STATUS_ACTIVATED = 1;
    public static final Integer ADMIN_USER_STATUS_FROZEN = 2;
    public static final Integer ADMIN_USER_STATUS_SOFT_DEL = 4;

    public static final Integer COMBO_HEAD_VALUE_AREA = -1;
    public static final String COMBO_HEAD_LABEL_AREA = "选择片区";

    public static final Integer SHOP_MAX_AD_NUM = 5;
    public static final Integer MALL_MAX_TOP_SHOP = 20;

    public static final String MD5 = "MD5";

    public static final Integer ACCOUNT_ROLE_SUPER = 1;
    public static final Integer ACCOUNT_ROLE_SYS = 2;
    public static final Integer ACCOUNT_ROLE_MALL = 3;
    public static final Integer ACCOUNT_ROLE_AREA = 4;

    public static final Integer QRCODE_MALL_TYPE = 1;
    public static final Integer QRCODE_SHOP_TYPE = 2;

    public static final Integer ACCOUNT_ROLE_SHOP = 5;

    public static final int CONNECT_SERVER_TIMEOUT = -2;
    public static final int CONNECT_SERVER_SUCCESS = 1;
    public static final int CONNECT_SERVER_FAILURE = -1;

    public static final int ORDER_STATUS_CANCELD = 4;
    public static final int ORDER_STATUS_REVIEWED = 3;
    public static final int ORDER_STATUS_FINISHED = 5;
    public static final int ORDER_STATUS_CONFIRMED = 1; //待支付

    public static final Integer REVIEW_AUDIT_PASS_STATUS = 1;//评论审核通过
    public static final Integer REVIEW_AUDIT_NOT_PASS_STATUS = 2;//评论审核不通过

    public static final Integer MALL_RECORD_INTERVAL_DAILY = 1;
    public static final Integer MALL_RECORD_INTERVAL_MONTHLY = 3;
    public static final Integer MALL_RECORD_INTERVAL_TODAY = 11;
    public static final Integer MALL_RECORD_INTERVAL_THIS_MONTH = 13;

    public static final Integer MALL_SETTLEMENT_STATUS_SETTLED = 1;

    public static final Integer SHOP_RECORD_INTERVAL_DAILY = 1;
    public static final Integer SHOP_RECORD_INTERVAL_MONTHLY = 3;
    public static final Integer SHOP_RECORD_INTERVAL_TODAY = 11;
    public static final Integer SHOP_RECORD_INTERVAL_THIS_MONTH = 13;

    public static final Integer SHOP_SETTLEMENT_STATUS_SETTLED = 1;

    public static final Integer ACT_READY_STATUS = 0;
    public static final Integer ACT_START_STATUS = 1;
    public static final Integer ACT_STOP_STATUS = 2;

    public static final String JOB_DETAIL_MSG_KEY = "msg";
    public static final String JOB_DETAIL_ACT_KEY = "act";
    public static final String YES = "true";

    public static final String JOB_DETAIL_SHOW_ACT_KEY = "showAct";
    public static final String JOB_DETAIL_REVIEW_KEY = "review";

    public static final int TRANSMISSION_TYPE = 2;

    public static final int PUSH_MSG_BY_APP = 1;
    public static final int PUSH_MSG_BY_MALL = 2;
    public static final int PUSH_MSG_BY_SHOP = 3;
    public static final Integer FLAG_JOIN_ACT_YES = 1;
    public static final String ITEM_STYLE_NAME_DEFAULT = "通用";
    
    public static final Integer COL_DEL_FLAG = 1;
    
    public static final String EXCEL_FILE_EXT_2003 = "xls";
    public static final String EXCEL_FILE_EXT_2007 = "xlsx";
    
    public static final Integer SHOP_STOP_STATUS = 2;
    public static final Integer ORDER_ADJUST_PRICE_USER_TYPE = 1;
}
