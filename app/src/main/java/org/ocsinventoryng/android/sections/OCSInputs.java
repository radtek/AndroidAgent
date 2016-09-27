package org.ocsinventoryng.android.sections;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;

import org.ocsinventoryng.android.actions.OCSLog;

import java.util.ArrayList;


public class OCSInputs implements OCSSectionInterface {
    final private String sectionTag = "INPUTS";
    public ArrayList<OCSInput> inputs;


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public OCSInputs(Context ctx) {
        OCSLog ocslog = OCSLog.getInstance();

        this.inputs = new ArrayList<OCSInput>();

        ocslog.debug("OCSInputs");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            ocslog.debug("OCSInputs BUILD Build.VERSION.SDK_INT ");
            Configuration config = ctx.getResources().getConfiguration();
            ocslog.debug("config.keyboard " + config.keyboard);
            ocslog.debug("config.touchscreen " + config.touchscreen);

            OCSInput inkb = new OCSInput();
            inkb.setType("keybord");
            switch (config.keyboard) {
                case Configuration.KEYBOARD_QWERTY:
                    inkb.setCaption("Keyboard querty");
                    break;
                case Configuration.KEYBOARD_12KEY:
                    inkb.setCaption("Keyboard 12 keys");
                    break;
                case Configuration.KEYBOARD_NOKEYS:
                    inkb.setCaption("No hardware keys");
                default:
                    break;
            }
            inputs.add(inkb);

            OCSInput ocsin = new OCSInput();
            ocsin.setType("Touchscreeen");
            switch (config.touchscreen) {
                case Configuration.TOUCHSCREEN_STYLUS:
                    ocsin.setCaption("Stylus touchscreen");
                    break;
                case Configuration.TOUCHSCREEN_FINGER:
                    ocsin.setCaption("Finger touchscreen");
                    break;
                case Configuration.TOUCHSCREEN_NOTOUCH:
                    ocsin.setCaption("NO touchscreen");
                    break;
                default:
                    break;
            }
            inputs.add(ocsin);
        }
/*
        // About cameras
		ocslog.debug("Search camera infos on build : "+Build.VERSION.SDK_INT );
		// Test if build < GINGERBREAD November 2010: Android 2.3
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
	    	OCSInput ocsci = new  OCSInput ();
	    	ocsci.setType("Camera");
	    	ocsci.setCaption("facing unknown");
	    	String sSz = getCameraMaxImgSize(openCamera());
	    	ocsci.setDescription("Image size "+sSz);
	    	inputs.add(ocsci);
		} else {
		    int numberOfCameras = Camera.getNumberOfCameras();
			ocslog.debug("Number of cameras : "+numberOfCameras);
		    CameraInfo cameraInfo = new CameraInfo();
		    for (int i = 0; i < numberOfCameras; i++) {
		    	OCSInput ocsci = new  OCSInput ();
		    	String sSz = getCameraMaxImgSize(openCamera(i));
		    	ocsci.setType("Camera");
		        Camera.getCameraInfo(i, cameraInfo);
		        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK)
		        	ocsci.setCaption("facing back");
		        else 
		        	ocsci.setCaption("facing front");
			    ocsci.setDescription("Image size "+sSz);
		    	inputs.add(ocsci);
		    }
		}
*/
        ocslog.debug("OCSInputs done");
    }

    // Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private Camera openCamera() {
        try {
            return Camera.open();
        } catch (RuntimeException e) {
            return null;
        }
    }

    // Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private Camera openCamera(int idx) {
        try {
            return Camera.open(idx);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String getCameraMaxImgSize(Camera cam) {
        if (cam == null) {
            return "busy";
        }
        Camera.Parameters params = cam.getParameters();
        long max_v = 0;
        Size max_sz = null;
        for (Size sz : params.getSupportedPictureSizes()) {
            long v = sz.height * sz.width;
            android.util.Log.d("OCSINPUT", String.valueOf(v));
            if (v > max_v) {
                max_v = v;
                max_sz = sz;
            }
        }
        cam.release();
        return String.valueOf(max_sz.width) + "x" + String.valueOf(max_sz.height);
    }

    public String toXML() {
        StringBuilder strOut = new StringBuilder();
        for (OCSInput o : inputs) {
            strOut.append(o.toXml());
        }
        return strOut.toString();
    }

    public String toString() {
        StringBuilder strOut = new StringBuilder();
        for (OCSInput o : inputs) {
            strOut.append(o.toString());
        }
        return strOut.toString();
    }

    public ArrayList<OCSSection> getSections() {
        ArrayList<OCSSection> lst = new ArrayList<OCSSection>();
        for (OCSInput o : inputs) {
            lst.add(o.getSection());
        }
        return lst;
    }

    public String getSectionTag() {
        return sectionTag;
    }
}
