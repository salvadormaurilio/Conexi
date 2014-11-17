package amigos.com.conexionarduino.util;

/**
 * Created by sati on 17/11/2014.
 */
public class ConstantsService {

    public static final int ARDUINO_USB_VENDOR_ID = 0x2341;
    public static final int ARDUINO_UNO_USB_PRODUCT_ID = 0x01;
    public static final int ARDUINO_MEGA_2560_USB_PRODUCT_ID = 0x10;
    public static final int ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID = 0x42;
    public static final int ARDUINO_UNO_R3_USB_PRODUCT_ID = 0x43;
    public static final int ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID = 0x44;
    public static final int ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID = 0x3F;
    public static final int ARDUINO_DUE_USB_PRODUCT_ID = 0x3D;

    public final static String DATA_RECEIVED_INTENT = "primavera.arduino.intent.action.DATA_RECEIVED";
    public final static String SEND_DATA_INTENT = "primavera.arduino.intent.action.SEND_DATA";
    public final static String DATA_SENT_INTERNAL_INTENT = "primavera.arduino.internal.intent.action.DATA_SENT";
    public final static String DATA_EXTRA = "primavera.arduino.intent.extra.DATA";
    public final static String USB_DEVICE_DETACHED = "primavera.arduino.intent.extra.DETACHED";


    public final static String TAG = "Comunicion con Arduino";
    public final static boolean DEBUG = true;



}
