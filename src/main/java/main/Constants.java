package main;

public class Constants {

    private Constants() {}

    public static final String COMMA = ",";

    public static final String DASH = "-";

    public static final String EMPTY = "";

    public static final String DATE_FORMAT = "yyyyMMdd hh:mm:ss";

    public static final String INPUT_DATE_FORMAT = "(\\d){4}-(\\d){2}-(\\d){2}";

    /**
     * 顯示錯誤的錯誤數量上限
     * */
    public static final int MAX_ERROR_QUANTITY = 1000;

    /**
     * 元件命名格式: JPanelName-JComponentName
     * */
    public static final String COMPONENT_NAME_FORMAT = "%s" + DASH + "%s";

    /**
     * 十五分鐘對應的微秒
     * */
    public static final int TIMEOUT = 1000 * 60 * 15;

    /**
     * 使用定時器時，以多少微秒作為間隔
     * */
    public static final int COUNTER_INTERVAL = 50;

    /**
     * 同一時間可以發送的最大API數量
     * */
    public static final int MAX_API_COUNT = 10;

    /*---------------------------------------------------------------------------------------------*/

    public static final int MAINFRAME_WIDTH = 750;

    public static final int MAINFRAME_HEIGHT = 600;

    public static final int UNIFORM_HEIGHT = 25;

    public static final String TITLE = "title";

    public static final int TITLE_X = 50;

    public static final int TITLE_Y = 50;

    public static final int TITLE_WIDTH = 200;

    public static final int TITLE_HEIGHT = UNIFORM_HEIGHT;

    public static final String CANCEL = "cancel";

    public static final String NEXT = "next";

    public static final int CANCEL_X = 450;

    public static final int NEXT_X = CANCEL_X + 150;

    public static final int CANCEL_Y = 450;

    public static final int NEXT_Y = CANCEL_Y;

    public static final int CANCEL_WIDTH = 100;

    public static final int NEXT_WIDTH = CANCEL_WIDTH;

    public static final int CANCEL_HEIGHT = UNIFORM_HEIGHT;

    public static final int NEXT_HEIGHT = CANCEL_HEIGHT;

    public static final int PROGRESS_BAR_X = 0;

    public static final int PROGRESS_BAR_Y = MAINFRAME_HEIGHT - 88;

    public static final int PROGRESS_BAR_WIDTH = MAINFRAME_WIDTH;

    public static final int PROGRESS_BAR_HEIGHT = UNIFORM_HEIGHT;

    public static final int TITLE_FONT_SIZE = 20;

    public static final int DEFAULT_FONT_SIZE = 15;

    public static final int TEXT_FIELD_FONT_SIZE = 12;

    public static final String FAIL = "fail";

    public static final String SUCCEED = "succeed";

    /*---------------------------------------------------------------------------------------------*/

    public static final String INDEX_PANEL = "index";

    public static final int INDEX_HEIGHT = UNIFORM_HEIGHT;

    public static final String INDEX_TITLE = TITLE;

    public static final String INDEX_START_DATE = "indexStartDate";

    public static final String INDEX_END_DATE = "indexEndDate";

    public static final String INDEX_LONGITUDE = "longitude";

    public static final String INDEX_LATITUDE = "latitude";

    public static final int INDEX_TEXT_FIELD_WIDTH = 150;

    public static final String INDEX_NEXT = NEXT;

    /*---------------------------------------------------------------------------------------------*/

    public static final String RESULT_PANEL = "result";

    public static final String RESULT_TITLE = TITLE;

    public static final String RESULT_CONTENT = "content";

    public static final String RESULT_SCROLL_PANE = "scroller";

    public static final String RESULT_DOWNLOAD_CHOOSER = "downloadChooser";

    public static final String RESULT_CANCEL = CANCEL;

    public static final String RESULT_NEXT = NEXT;

    /*---------------------------------------------------------------------------------------------*/

    public static final String PROGRESS_BAR_PANEL = "progressBarPanel";

    public static final String STATUS_LABEL = "statusLabel";

    public static final String PROGRESS_BAR = "progressBar";

    /*---------------------------------------------------------------------------------------------*/

    public static final String BUTTON_DOWNLOAD = "下載報表";

    public static final String BUTTON_CANCEL = "上一步";

    public static final String BUTTON_NEXT = "下一步";

    /*---------------------------------------------------------------------------------------------*/

    public static final String LABEL_INDEX_TITLE = "設定報表資訊";

    public static final String LABEL_START = "起始日期：";

    public static final String LABEL_END = "結束日期：";

    public static final String LABEL_DATE_HINT = "日期格式為yyyy-MM-dd";

    public static final String LABEL_TO = "至";

    public static final String LABEL_LONGITUDE = "經度：";

    public static final String LABEL_LATITUDE = "緯度：";

    public static final String LABEL_RESULT = "執行結果";

    /*---------------------------------------------------------------------------------------------*/

    public static final String MSG_INPUT_DATE_FORMAT_ERROR = "格式錯誤";

    public static final String MSG_INPUT_DATE_ERROR = "日期錯誤";

    public static final String MSG_COORDINATE_ERROR = "座標錯誤";

    public static final String MSG_DOWNLOAD_DATA_FORMAT = "下載%s";

    public static final String MSG_DOWNLOAD_DATA = "下載資料";

    public static final String MSG_DOWNLOAD_DATA_ERROR_FORMAT = MSG_DOWNLOAD_DATA_FORMAT + "錯誤";

    public static final String MSG_GENERATE_EXCEL = "產生Excel";

    public static final String MSG_DOWNLOAD_DATA_FAIL = "下載資料失敗";

    public static final String MSG_DOWNLOAD_DATA_SUCCEED = "下載資料成功";

    public static final String MSG_DOWNLOAD_FILE_CANCEL = "下載取消";

    public static final String MSG_DOWNLOAD_FILE_FAIL = "下載失敗";

    public static final String MSG_LOAD_FOLDER_FAIL = "讀取資料夾失敗";

    public static final String MSG_REPORT_IS_NULL = "報表為空";

    public static final String MSG_DOWNLOAD_SUCCEED = "下載成功";

    public static final String MSG_CREATE_ZIP = "產生壓縮檔";

    public static final String MSG_HANDLE_ZIP = "處理壓縮資料";

    /*---------------------------------------------------------------------------------------------*/

    public static final String NUMBER_MIN = "minimum";

    public static final String NUMBER_MAX = "maximum";

    public static final String NUMBER_AVERAGE = "average";

    public static final String NUMBER_STANDARD_DEVIATION = "standard deviation";

    /*---------------------------------------------------------------------------------------------*/

    public static final String LOG_FILE_NAME = "log.txt";

    public static final String ZIP_FILE_NAME = "報表.zip";

    public static final String ZIP_FILE_NAME_FORMAT = "報表(%s).zip";

    /*---------------------------------------------------------------------------------------------*/

    public static final String EXCEL_NUMERIC_CELL_DATA_FORMAT_INTEGER = "#,##0";

    public static final String EXCEL_NUMERIC_CELL_DATA_FORMAT = "#,##0.000";

    public static final String EXCEL_FILE_EXTENSION = ".xlsx";

    public static final String EXCEL_FILE_NAME = "報表" + EXCEL_FILE_EXTENSION;

    public static final int EXCEL_WORD_WIDTH = 256;

    public static final int EXCEL_ROW_TITLE = 0;

    public static final int EXCEL_ROW_MIN = EXCEL_ROW_TITLE + 1;

    public static final int EXCEL_ROW_MAX = EXCEL_ROW_MIN + 1;

    public static final int EXCEL_ROW_AVERAGE = EXCEL_ROW_MAX + 1;

    public static final int EXCEL_ROW_STANDARD_DEVIATION = EXCEL_ROW_AVERAGE + 1;

    public static final int EXCEL_COLUMN_TIME = 0;

    public static final int EXCEL_COLUMN_TEMPERATURE = EXCEL_COLUMN_TIME + 1;

    public static final int EXCEL_COLUMN_APPARENT_TEMPERATURE = EXCEL_COLUMN_TEMPERATURE + 1;

    public static final int EXCEL_COLUMN_RELATIVE_HUMIDITY = EXCEL_COLUMN_APPARENT_TEMPERATURE + 1;

    public static final int EXCEL_COLUMN_WIND_SPEED = EXCEL_COLUMN_RELATIVE_HUMIDITY + 1;

    public static final int EXCEL_COLUMN_WIND_DIRECTION = EXCEL_COLUMN_WIND_SPEED + 1;

    public static final int EXCEL_COLUMN_PRECIPITATION = EXCEL_COLUMN_WIND_DIRECTION + 1;

    public static final int EXCEL_COLUMN_PRECIPITATION_PROBABILITY = EXCEL_COLUMN_PRECIPITATION + 1;

    public static final int EXCEL_COLUMN_RAIN = EXCEL_COLUMN_PRECIPITATION_PROBABILITY + 1;

    public static final int EXCEL_COLUMN_SHOWERS = EXCEL_COLUMN_RAIN + 1;

    /*---------------------------------------------------------------------------------------------*/

    public static final String DATA_TYPE_TIME = "time";

    public static final String DATA_TYPE_TEMPERATURE = "temperature_2m";

    public static final String DATA_TYPE_APPARENT_TEMPERATURE = "apparent_temperature";

    public static final String DATA_TYPE_RELATIVE_HUMIDITY = "relative_humidity_2m";

    public static final String DATA_TYPE_WIND_SPEED = "wind_speed_10m";

    public static final String DATA_TYPE_WIND_DIRECTION = "wind_direction_10m";

    public static final String DATA_TYPE_PRECIPITATION = "precipitation";

    public static final String DATA_TYPE_PRECIPITATION_PROBABILITY = "precipitation_probability";

    public static final String DATA_TYPE_RAIN = "rain";

    public static final String DATA_TYPE_SHOWERS = "showers";

    /*---------------------------------------------------------------------------------------------*/

    public static final double LONGITUDE_TOKYO = 139.8394;

    public static final double LATITUDE_TOKYO = 35.6528;

    public static final double LONGITUDE_OSAKA = 135.3008;

    public static final double LATITUDE_OSAKA = 34.4138;

}