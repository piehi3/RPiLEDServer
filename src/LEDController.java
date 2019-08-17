import com.pi4j.io.gpio.*;
import com.pi4j.platform.PlatformAlreadyAssignedException;

public class LEDController {
    private static final Pin RED_PIN = RaspiPin.GPIO_02;//pinlayouts as gpio numbers
    private static final Pin GREEN_PIN = RaspiPin.GPIO_03;
    private static final Pin BLUE_PIN = RaspiPin.GPIO_04;
	
	private static GpioController gpio;
	
    private static int red;
    private static int green;
    private static int blue;
	
	private static GpioPinPwmOutput pwm_red;
	private static GpioPinPwmOutput pwm_green;
	private static GpioPinPwmOutput pwm_blue;

    public static void setup() throws  InterruptedException{
		
		gpio = GpioFactory.getInstance();//gpio wrapper

        //setup the pins for pwm output
        pwm_red = gpio.provisionSoftPwmOutputPin(RED_PIN);
		pwm_green = gpio.provisionSoftPwmOutputPin(GREEN_PIN);
		pwm_blue = gpio.provisionSoftPwmOutputPin(BLUE_PIN);
		
		//sets pwm to base state
		setLED(0,0,0);
    }

    public static void setLED(int r,int g,int b){
        pwm_red.setPwm(r);
        red=r;
        pwm_green.setPwm(g);
        green = g;
        pwm_blue.setPwm(b);
        blue = b;
    }

    public static void cleanup(){
        setLED(0,0,0);
		gpio.shutdown();
    }

	public static int[] getRGB(){
		int[] rgb = {red,green,blue};
		return rgb;
	}
	
}
