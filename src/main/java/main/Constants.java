package main;

public class Constants {

    private Constants() {}

    public final static String COMMA = ",";

    public final static String DASH = "-";

    public final static String EMPTY = "";

    public final static String DATE_FORMAT = "yyyyMMdd hh:mm:ss";

    public final static String INPUT_DATE_FORMAT = "(\\d){4}-(\\d){2}-(\\d){2}";

    /**
     * 顯示錯誤的錯誤數量上限
     * */
    public final static int MAX_ERROR_QUANTITY = 1000;

    /**
     * 元件命名格式: JPanelName-JComponentName
     * */
    public final static String COMPONENT_NAME_FORMAT = "%s" + DASH + "%s";

    /**
     * 十五分鐘對應的微秒
     * */
    public final static int QUARTER = 1000 * 60 * 15;

    /**
     * 使用定時器時，以多少微秒作為間隔
     * */
    public final static int COUNTER_INTERVAL = 40;

    /*---------------------------------------------------------------------------------------------*/

    public final static int MAINFRAME_WIDTH = 750;

    public final static int MAINFRAME_HEIGHT = 600;

    public final static int UNIFORM_HEIGHT = 25;

    public final static String TITLE = "title";

    public final static int TITLE_X = 50;

    public final static int TITLE_Y = 50;

    public final static int TITLE_WIDTH = 200;

    public final static int TITLE_HEIGHT = UNIFORM_HEIGHT;

    public final static String CANCEL = "cancel";

    public final static String NEXT = "next";

    public final static int CANCEL_X = 450;

    public final static int NEXT_X = CANCEL_X + 150;

    public final static int CANCEL_Y = 450;

    public final static int NEXT_Y = CANCEL_Y;

    public final static int CANCEL_WIDTH = 100;

    public final static int NEXT_WIDTH = CANCEL_WIDTH;

    public final static int CANCEL_HEIGHT = UNIFORM_HEIGHT;

    public final static int NEXT_HEIGHT = CANCEL_HEIGHT;

    public final static int PROGRESS_BAR_X = 0;

    public final static int PROGRESS_BAR_Y = MAINFRAME_HEIGHT - 88;

    public final static int PROGRESS_BAR_WIDTH = MAINFRAME_WIDTH;

    public final static int PROGRESS_BAR_HEIGHT = UNIFORM_HEIGHT;

    public final static int TITLE_FONT_SIZE = 20;

    public final static int DEFAULT_FONT_SIZE = 15;

    public final static int TEXT_FIELD_FONT_SIZE = 12;

    public final static String FAIL = "fail";

    public final static String SUCCEED = "succeed";

    /*---------------------------------------------------------------------------------------------*/

    public final static String INDEX_PANEL = "index";

    public final static int INDEX_HEIGHT = UNIFORM_HEIGHT;

    public final static String INDEX_TITLE = TITLE;

    public final static String INDEX_START_DATE = "indexStartDate";

    public final static String INDEX_END_DATE = "indexEndDate";

    public final static String INDEX_LONGITUDE = "longitude";

    public final static String INDEX_LATITUDE = "latitude";

    public final static int INDEX_TEXT_FIELD_WIDTH = 150;

    public final static String INDEX_NEXT = NEXT;

    /*---------------------------------------------------------------------------------------------*/

    public final static String RESULT_PANEL = "result";

    public final static String RESULT_TITLE = TITLE;

    public final static String RESULT_CONTENT = "content";

    public final static String RESULT_SCROLL_PANE = "scroller";

    public final static String RESULT_DOWNLOAD_CHOOSER = "downloadChooser";

    public final static String RESULT_CANCEL = CANCEL;

    public final static String RESULT_NEXT = NEXT;

    /*---------------------------------------------------------------------------------------------*/

    public final static String PROGRESS_BAR_PANEL = "progressBarPanel";

    public final static String STATUS_LABEL = "statusLabel";

    public final static String PROGRESS_BAR = "progressBar";

    /*---------------------------------------------------------------------------------------------*/

    public final static String BUTTON_DOWNLOAD = "下載報表";

    public final static String BUTTON_CANCEL = "上一步";

    public final static String BUTTON_NEXT = "下一步";

    /*---------------------------------------------------------------------------------------------*/

    public final static String LABEL_INDEX_TITLE = "設定報表資訊";

    public final static String LABEL_START = "起始日期：";

    public final static String LABEL_END = "結束日期：";

    public final static String LABEL_DATE_HINT = "日期格式為yyyy-MM-dd";

    public final static String LABEL_TO = "至";

    public final static String LABEL_LONGITUDE = "經度：";

    public final static String LABEL_LATITUDE = "緯度：";

    public final static String LABEL_RESULT = "執行結果";

    /*---------------------------------------------------------------------------------------------*/

    public final static String MSG_INPUT_DATE_FORMAT_ERROR = "格式錯誤";

    public final static String MSG_INPUT_DATE_ERROR = "日期錯誤";

    public final static String MSG_COORDINATE_ERROR = "座標錯誤";

    public final static String MSG_DOWNLOAD_DATA_FORMAT = "下載%s";

    public final static String MSG_DOWNLOAD_DATA_ERROR_FORMAT = MSG_DOWNLOAD_DATA_FORMAT + "錯誤";

    public final static String MSG_GENERATE_EXCEL = "產生Excel";

    public final static String MSG_DOWNLOAD_DATA_FAIL = "下載資料失敗";

    public final static String MSG_DOWNLOAD_DATA_SUCCEED = "下載資料成功";

    public final static String MSG_DOWNLOAD_FILE_CANCEL = "下載取消";

    public final static String MSG_DOWNLOAD_FILE_FAIL = "下載失敗";

    public final static String MSG_LOAD_FOLDER_FAIL = "讀取資料夾失敗";

    public final static String MSG_REPORT_IS_NULL = "報表為空";

    public final static String MSG_DOWNLOAD_SUCCEED = "下載成功";

    public final static String MSG_CREATE_ZIP = "產生壓縮檔";

    public final static String MSG_HANDLE_ZIP = "處理壓縮資料";

    /*---------------------------------------------------------------------------------------------*/

    public final static String NUMBER_MIN = "minimum";

    public final static String NUMBER_MAX = "maximum";

    public final static String NUMBER_AVERAGE = "average";

    public final static String NUMBER_STANDARD_DEVIATION = "standard deviation";

    /*---------------------------------------------------------------------------------------------*/

    public final static String LOG_FILE_NAME = "log.txt";

    public final static String ZIP_FILE_NAME = "報表.zip";

    public final static String ZIP_FILE_NAME_FORMAT = "報表(%s).zip";

    /*---------------------------------------------------------------------------------------------*/

    public final static String EXCEL_NUMERIC_CELL_DATA_FORMAT_INTEGER = "#,##0";

    public final static String EXCEL_NUMERIC_CELL_DATA_FORMAT = "#,##0.000";

    public final static String EXCEL_FILE_EXTENSION = ".xlsx";

    public final static String EXCEL_FILE_NAME = "報表" + EXCEL_FILE_EXTENSION;

    public final static int EXCEL_WORD_WIDTH = 256;

    public final static int EXCEL_ROW_TITLE = 0;

    public final static int EXCEL_ROW_MIN = EXCEL_ROW_TITLE + 1;

    public final static int EXCEL_ROW_MAX = EXCEL_ROW_MIN + 1;

    public final static int EXCEL_ROW_AVERAGE = EXCEL_ROW_MAX + 1;

    public final static int EXCEL_ROW_STANDARD_DEVIATION = EXCEL_ROW_AVERAGE + 1;

    public final static int EXCEL_COLUMN_TIME = 0;

    public final static int EXCEL_COLUMN_TEMPERATURE = EXCEL_COLUMN_TIME + 1;

    public final static int EXCEL_COLUMN_APPARENT_TEMPERATURE = EXCEL_COLUMN_TEMPERATURE + 1;

    public final static int EXCEL_COLUMN_RELATIVE_HUMIDITY = EXCEL_COLUMN_APPARENT_TEMPERATURE + 1;

    public final static int EXCEL_COLUMN_WIND_SPEED = EXCEL_COLUMN_RELATIVE_HUMIDITY + 1;

    public final static int EXCEL_COLUMN_WIND_DIRECTION = EXCEL_COLUMN_WIND_SPEED + 1;

    public final static int EXCEL_COLUMN_PRECIPITATION = EXCEL_COLUMN_WIND_DIRECTION + 1;

    public final static int EXCEL_COLUMN_PRECIPITATION_PROBABILITY = EXCEL_COLUMN_PRECIPITATION + 1;

    public final static int EXCEL_COLUMN_RAIN = EXCEL_COLUMN_PRECIPITATION_PROBABILITY + 1;

    public final static int EXCEL_COLUMN_SHOWERS = EXCEL_COLUMN_RAIN + 1;

    /*---------------------------------------------------------------------------------------------*/

    public final static String DATA_TYPE_TIME = "time";

    public final static String DATA_TYPE_TEMPERATURE = "temperature_2m";

    public final static String DATA_TYPE_APPARENT_TEMPERATURE = "apparent_temperature";

    public final static String DATA_TYPE_RELATIVE_HUMIDITY = "relative_humidity_2m";

    public final static String DATA_TYPE_WIND_SPEED = "wind_speed_10m";

    public final static String DATA_TYPE_WIND_DIRECTION = "wind_direction_10m";

    public final static String DATA_TYPE_PRECIPITATION = "precipitation";

    public final static String DATA_TYPE_PRECIPITATION_PROBABILITY = "precipitation_probability";

    public final static String DATA_TYPE_RAIN = "rain";

    public final static String DATA_TYPE_SHOWERS = "showers";

    /*---------------------------------------------------------------------------------------------*/

    public final static double LONGITUDE_TOKYO = 139.8394;

    public final static double LATITUDE_TOKYO = 35.6528;

    public final static double LONGITUDE_OSAKA = 135.3008;

    public final static double LATITUDE_OSAKA = 34.4138;

}