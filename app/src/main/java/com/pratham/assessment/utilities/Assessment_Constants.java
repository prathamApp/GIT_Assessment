package com.pratham.assessment.utilities;

public class Assessment_Constants {
    public static final String INITIAL_ENTRIES = "initial_entries";
    public static final String KEY_ASSET_COPIED = "key_asset_copied";
    public static final String KEY_MENU_COPIED = "KEY_MENU_COPIED";
    public static final String FILE_DOWNLOAD_COMPLETE = "file_download_complete";
    public static final String FILE_DOWNLOAD_ERROR = "file_download_error";
    public static final String FILE_DOWNLOAD_UPDATE = "file_download_update";
    public static final String FILE_DOWNLOAD_STARTED = "file_download_started";
    public static final String GAME = "game";
    public static final String LANGUAGE = "language";
    public static boolean SD_CARD_Content = false;
    public static String STORING_IN;
    public static String ext_path="";
    public static String ENGLISH = "English";

    public static String ASSESSMENT_FOLDER_PATH = "/.Assessment/";
    public static String GAME_PATH = "/.Assessment/English/Game/";
    public static String ENGLISH_FOLDER_PATH = "/.Assessment/English/";
    public static String THUMBS_PATH = "/.Assessment/English/app_Thumbs/";

    public static String CERTIFICATE_LBL = "certificate_lbl";
    public static final String PREFS_VERSION = "com.pratham.assessment";
    public static final String CURRENT_VERSION = "App Version";

    public static final String INTERNET_DOWNLOAD = "prathmApp";
    public static final String INTERNET_DOWNLOAD_API = "http://www.prodigi.openiscool.org/api/pos/Get?id=";
    public static final String INTERNET_DOWNLOAD_RESOURCE = "downloadResource";
    public static final String INTERNET_DOWNLOAD_RESOURCE_API = "http://prodigi.openiscool.org/api/pos/DownloadResource?resid=";


    public static final String PRATHAM_KOLIBRI_HOTSPOT = "prathamkolibri";
    public static String FACILITY_ID = "facility_id";
    public static final String USAGEDATA = "USAGEDATA";
    public static final String BASE_URL = "http://prodigi.openiscool.org/api/pos/";
    public static String RASP_IP = "http://192.168.4.1:8080";
    public static final String SCORE_COUNT = "ScoreCount";
    public static final String STUDENTS = "students";
    public static final String METADATA = "metadata";
    public static final String SESSION = "session";
    public static final String SCORE = "scores";
    public static final String LOGS = "logs";
    public static final String ASSESSMENT = "assessment";
    public static final String SUPERVISOR = "supervisor";
    public static final String LEARNTWORDS = "learntwords";

    public static final String ATTENDANCE = "attendances";
    public static final String GROUPID = "groupid";
    public static final String GROUPID1 = "group1";
    public static final String GROUPID2 = "group2";
    public static final String GROUPID3 = "group3";
    public static final String GROUPID4 = "group4";
    public static final String GROUPID5 = "group5";
    public static final String GROUP_AGE_BELOW_7 = "GROUP_AGE_BELOW_7";
    public static final String GROUP_AGE_ABOVE_7 = "GROUP_AGE_ABOVE_7";
    public static final String STUDENT_LIST = "STUDENT_LIST";    public static final String SESSIONID = "sessionid";
    public static String currentStudentID;
    public static String currentSession;
    public static boolean supervisedAssessment = false;
    public static String assessmentSession = "";
    public static String currentSupervisorID = "";


    public static enum URL {
        BROWSE_BY_ID(BASE_URL + "get?id="),
        SEARCH_BY_KEYWORD(BASE_URL + "GetSearchList?"),
        POST_GOOGLE_DATA(BASE_URL + "PostGoogleSignIn"),
        GET_TOP_LEVEL_NODE(BASE_URL + "GetTopLevelNode?lang="),
        DATASTORE_RASPBERY_URL(RASP_IP + "/pratham/datastore/"),
        BROWSE_RASPBERRY_URL(RASP_IP + "/api/contentnode?parent="),
        GET_RASPBERRY_HEADER(RASP_IP + "/api/contentnode?content_id=f9da12749d995fa197f8b4c0192e7b2c"),
        POST_SMART_INTERNET_URL("http://www.rpi.prathamskills.org/api/pushdatasmartphone/post/"),
        POST_TAB_INTERNET_URL("http://www.rpi.prathamskills.org/api/pushdatapradigi/post/"),
        DOWNLOAD_RESOURCE(BASE_URL + "DownloadResource?resid=");
        public static final String SCORE_COUNT = "ScoreCount";
        public static final String STUDENTS = "students";
        public static final String METADATA = "metadata";

        private final String name;

        private URL(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return name;
        }

    }

}
