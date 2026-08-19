package com.cgnpc.bbxpark.common.constant;

public class Constants {
    /**
     * 私有构造函数.
     */
    private Constants() {

    }

    /**
     * 性别：对应Sex中的unknown.
     */
    public static final short  SEX_UNKNOWN    = 0;
    /**
     * 性别：对应Sex中的male.
     */
    public static final short  SEX_MALE       = 1;
    /**
     * 性别：对应Sex中的female.
     */
    public static final short  SEX_FEMALE     = 2;
    /**
     * 性别：对应Sex中的secret.
     */
    public static final short  SEX_SECRET     = 3;
    /**
     * 性别：未知.
     */
    public static final String SEX_UNKNOWN_CN = "未知";
    /**
     * 性别：男.
     */
    public static final String SEX_MALE_CN    = "男";
    /**
     * 性别：女.
     */
    public static final String SEX_FEMALE_CN  = "女";
    /**
     * 性别：保密.
     */
    public static final String SEX_SECRET_CN  = "保密";

    /**
     * 表示是否,对应的是IsOrNot中的yes.
     */
    public static final short ISORNOT_YES = 1;

    /**
     * 表示是否,对应的是IsOrNot中的no.
     */
    public static final short ISORNOT_NO = 0;

    /**
     * 表示是.
     */
    public static final String ISORNOT_YES_CN  = "是";
    /**
     * 表示否.
     */
    public static final String ISORNOT_NO_CN  = "否";

    /**
     * 表示成功,对应的是SuccOrFail中的success.
     */
    public static final short SUCCORFAIL_SUCC = 1;

    /**
     * 表示失败,对应的是SuccOrFail中的fail.
     */
    public static final short  SUCCORFAIL_FAIL    = 0;
    /**
     * 登录状态：成功.
     */
    public static final String SUCCORFAIL_SUCC_CN = "成功";
    /**
     * 登录状态：失败.
     */
    public static final String SUCCORFAIL_FAIL_CN = "失败";

    /**
     * 表示启用，对应的是Status.enabled.
     */
    public static final int STATUS_ENABLED = 1;

    /**
     * 表示禁用，对应的是Status.disabled.
     */
    public static final int STATUS_DISABLED = 0;

    /**
     * 表示注销，对应的是Status.deletion.
     */
    public static final int STATUS_DELETION = 2;

    /**
     * 表示启用，对应的是Status.enabled.
     */
    public static final String STATUS_ENABLED_CN = "启用";

    /**
     * 表示禁用，对应的是Status.disabled.
     */
    public static final String STATUS_DISABLED_CN = "禁用";

    /**
     * 表示注销，对应的是Status.deletion.
     */
    public static final String STATUS_DELETION_CN = "注销";

    /**
     * 数据类型：对应DataType中的system.
     */
    public static final short  DATA_TYPE_SYSTEM    = 0;

    /**
     * 数据类型：对应DataType中的customize.
     */
    public static final short  DATA_TYPE_CUSTOMIZE    = 1;

    /**
     * 数据类型：系统配置.
     */
    public static final String DATA_TYPE_SYSTEM_CN = "系统";
    /**
     * 数据类型：自定义配置.
     */
    public static final String DATA_TYPE_CUSTOMIZE_CN = "自定义";

    /**
     * 表示未处理与已处理，Processed.no.
     */
    public static final short PROCESSED_NO = 0;

    /**
     * 表示未处理与已处理，Processed.yes.
     */
    public static final short PROCESSED_YES = 1;

    /**
     * 表示已删除与有效与无效，IsInvalid.deleted.
     */
    public static final short ISINVALID_DELETED = -1;

    /**
     * 表示已删除与有效与无效，IsInvalid.invalid.
     */
    public static final short ISINVALID_INVALID = 0;

    /**
     * 表示已删除与有效与无效，IsInvalid.valid.
     */
    public static final short ISINVALID_VALID = 1;

    /**
     * 附件类型[文本].
     */
    public static final String FILETYPE_TXT = "TXT";

    /**
     * 附件类型[excel].
     */
    public static final String FILETYPE_EXCEL = "XLS|XLSX";

    /**
     * 附件类型[word].
     */
    public static final String FILETYPE_WORD = "DOC|DOCX";

    /**
     * 附件类型[powerpoint].
     */
    public static final String FILETYPE_PPT = "PPT|PPTX";

    /**
     * 附件类型[pdf].
     */
    public static final String FILETYPE_PDF = "PDF";

    /**
     * 附件类型[flash].
     */
    public static final String FILETYPE_FLASH = "SWF";

    /**
     * 附件类型[music].
     */
    public static final String FILETYPE_MUSIC = "MP3|WMA|WAV|M4A|MID|AAC";

    /**
     * 附件类型[film].
     */
    public static final String FILETYPE_FILM = "MP4|MPEG|AVI|WMV|3GP|MKV|FLV|RMVB";

    /**
     * 附件类型[pic].
     */
    public static final String FILETYPE_IMAGE = "GIF|JPG|PNG|BMP|TIF|JPEG";

    /**
     * 附件类型[db].
     */
    public static final String FILETYPE_ACCESS = "ACCDB";

    /**
     * 查询列表接口-分页.
     */
    public static final String DATA_LIST_PAGE = "/list/page";

    /**
     * 查询列表接口-分页.
     */
    public static final String DATA_LIST = "/list";

    /**
     * 新增数据.
     */
    public static final String DATA_ADD = "/add";

    /**
     * 新增数据-批量.
     */
    public static final String DATA_ADD_BATCH = "/add/batch";

    /**
     * 删除数据-批量.
     */
    public static final String DATA_DELETE_BATCH = "/remove/batch";

    /**
     * 默认分页起始页.
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * 默认分页大小.
     */
    public static final int DEFAULT_PAGE_LIMIT = 10;
}
