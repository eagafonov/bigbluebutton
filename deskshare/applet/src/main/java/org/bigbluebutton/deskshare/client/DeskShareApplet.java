/** 
* ===License Header===
*
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
*
* Copyright (c) 2010 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 2.1 of the License, or (at your option) any later
* version.
*
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
* 
* ===License Header===
*/
package org.bigbluebutton.deskshare.client;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Image;
import netscape.javascript.*; // add plugin.jar to classpath during compilation
import java.lang.StringBuilder;

public class DeskShareApplet extends JApplet implements ClientListener {
	public static final String NAME = "DESKSHAREAPPLET: ";
	
	private static final long serialVersionUID = 1L;

    String appletId = "";
	String hostValue = "localhost";
    Integer portValue = new Integer(9123);
    String roomValue = "85115";
    Integer cWidthValue = new Integer(800);
    Integer cHeightValue = new Integer(600);
    Integer sWidthValue = new Integer(800);
    Integer sHeightValue = new Integer(600);   
    Boolean qualityValue = false;
    Boolean aspectRatioValue = false;
    Integer xValue = new Integer(-1);
    Integer yValue = new Integer(-1);
    Boolean tunnelValue = true;
    Boolean fullScreenValue = false;
    DeskshareClient client;
    Image icon;

    public boolean isSharing = false;
    private JSObject wrapper = null;
    String cookieFormat = "1";

    public String readCookie() {
        String data = "";

        String cookiename = "__deskshareAppletData";

        JSObject myBrowser = JSObject.getWindow(this);
        JSObject myDocument = (JSObject) myBrowser.getMember("document");

        String myCookie = (String) myDocument.getMember("cookie");

        if (myCookie.length() > 0) {
            String[] cookies = myCookie.split(";");

            for (String cookie : cookies) {
                String[] name_val = cookie.split("=", 2);

                System.out.printf("Cookie name_val '%s' %d\n", name_val[0], name_val.length);

                if (name_val[0].trim().equals(cookiename)) {
                    if (name_val.length > 1) {
                        data = name_val[1];
                    }
                    break;
                }
            }
        }

        return data;
    }

    public void writeCookie(String cookieData) {
        String cookiename = "__deskshareAppletData";

        JSObject myBrowser = JSObject.getWindow(this);
        JSObject myDocument = (JSObject) myBrowser.getMember("document");

        myDocument.setMember("cookie", cookiename + "=" + cookieData);
    }

    public void onSizePositionChange(int x, int y, int w, int h) {
        StringBuilder sb = new StringBuilder();

        sb.append(cookieFormat).append("#").append(x).append('#').append(y).append('#').append(w).append('#').append(h);

        writeCookie(sb.toString());
    };

    @Override
	public void init() {		
        System.out.println("Desktop Sharing Applet Initializing");

        appletId = getParameter("ID");

        JSObject window = (JSObject)JSObject.getWindow(this);

        this.wrapper = (JSObject)((JSObject)window.getMember("bbbDeskshare")).call("getHandler", new Object[]{appletId});

        jsCall("onAppletInit");

		hostValue = getParameter("IP");
		String port = getParameter("PORT");
		if (port != null) portValue = Integer.parseInt(port);
		roomValue = getParameter("ROOM");

		String captureFullScreen = getParameter("FULL_SCREEN");
		if (captureFullScreen != null) fullScreenValue = Boolean.parseBoolean(captureFullScreen);

		String tunnel = getParameter("HTTP_TUNNEL");
		if (tunnel != null) tunnelValue = Boolean.parseBoolean(tunnel);

        String tryIconPath = getParameter("TRAY_ICON");

        if (tryIconPath != null) {
            icon = getImage(getCodeBase(), tryIconPath);
        }


        String cookie = readCookie();

        String[] cookie_data = cookie.split("#"); // VERSION#X#Y#WIDTH#HEIGHT

        if (cookie_data.length == 5 && cookie_data[0].equals(cookieFormat)) {
            // X#Y#WIDTH#HEIGHT

            xValue = Integer.parseInt( cookie_data[1] );
            yValue = Integer.parseInt( cookie_data[2] );
            sWidthValue = cWidthValue = Integer.parseInt( cookie_data[3] );
            sHeightValue = cHeightValue = Integer.parseInt( cookie_data[4] );

//             System.out.println("Got initial size/position from cookie");
        } else {
//             System.out.printf("Init size/position cookie ('%s', '%s')\n", cookie_data.length, cookie, cookie_data[0]);
            onSizePositionChange(xValue, yValue, sWidthValue, sHeightValue);
        }
	}
		
	@Override
	public void start() {		 	
		System.out.println("Desktop Sharing Applet Starting");
		super.start();

		client = new DeskshareClient.NewBuilder().host(hostValue).port(portValue)
					.room(roomValue).captureWidth(cWidthValue)
					.captureHeight(cHeightValue).scaleWidth(sWidthValue).scaleHeight(sHeightValue)
					.quality(qualityValue).aspectRatio(aspectRatioValue)
					.x(xValue).y(yValue).fullScreen(fullScreenValue)
					.httpTunnel(tunnelValue).trayIcon(icon).enableTrayIconActions(false).build();
		client.addClientListener(this);
		client.start();
        jsCall("onAppletStart");
	}
			
	@Override
	public void destroy() {
		System.out.println("Desktop Sharing Applet Destroy");
		client.stop();
		super.destroy();
	}

	@Override
	public void stop() {
		System.out.println("Desktop Sharing Applet Stopping");
		client.stop();	
		super.stop();
	}
	
	public void onClientStop(ExitCode reason) {
        jsCall("onAppletStop");

		// determine if client is disconnected _PTS_272_
		if ( ExitCode.CONNECTION_TO_DESKSHARE_SERVER_DROPPED == reason ){
			JFrame pframe = new JFrame("Desktop Sharing Disconneted");
			if ( null != pframe ){
				client.disconnected();
				JOptionPane.showMessageDialog(pframe,
					"Disconnected. Reason: Lost connection to the server." + reason ,
					"Disconnected" ,JOptionPane.ERROR_MESSAGE );
			}else{
				System.out.println("Desktop sharing allocate memory failed.");
			}
		}else{
			client.stop();
		}
	}

    private JSObject jsCall(java.lang.String methodName) {
        return jsCall(methodName, new Object[] {});
    }

    private JSObject jsCall(java.lang.String methodName, java.lang.Object[] args) {
        JSObject retval = null;

        if (this.wrapper != null) {
            try {
                retval = (JSObject)this.wrapper.call(methodName, args);
            }
            catch (JSException e) {
                System.err.printf("Got JS exception: ", e.getMessage());
            }
        } else {
            System.err.printf("Can't call method '%s': not JS handler defined\n", methodName);
        }

        return null;
    }

    public void onPublishStart() {
        jsCall("onShareStart");
    };
}
