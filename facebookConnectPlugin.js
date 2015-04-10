"use strict";

/*
 * @author Ally Ogilvie
 * @copyright Wizcorp Inc. [ Incorporated Wizards ] 2014
 * @file - facebookConnectPlugin.js
 * @about - JavaScript interface for PhoneGap bridge to Facebook Connect SDK
 *
 *
 */

if (cordova.platformId == "browser") {

    var facebookConnectPlugin = {

        logEvent: function (eventName, params, valueToSum, s, f) {
            // AppEvents are not avaliable in JS.
            s();
        },

        // Browser wrapper API ONLY
        browserInit: function (appId, version) {
            if (!version) {
                version = "v2.0";
            }
            FB.init({
                appId      : appId,
                cookie     : true,
                xfbml      : true,
                version    : version
            });
        }
    };
    
    // Bake in the JS SDK
    (function () {
        if (!window.FB) {
            console.log("launching FB SDK");
            var e = document.createElement('script');
            e.src = document.location.protocol + '//connect.facebook.net/en_US/sdk.js';
            e.async = true;
            document.getElementById('fb-root').appendChild(e);
            if (!window.FB) {
                // Probably not on server, use the sample sdk
                e.src = 'phonegap/plugin/facebookConnectPlugin/fbsdk.js';
                document.getElementById('fb-root').appendChild(e);
                console.log("Attempt local load: ", e);
            }
        }
    }());

    module.exports = facebookConnectPlugin;

} else {

    var exec = require("cordova/exec");

    var facebookConnectPlugin = {

        logEvent: function(name, params, valueToSum, s, f) {
            // Prevent NSNulls getting into iOS, messes up our [command.argument count]
            if (!params && !valueToSum) {
                exec(s, f, "FacebookConnectPlugin", "logEvent", [name]);
            } else if (params && !valueToSum) {
                exec(s, f, "FacebookConnectPlugin", "logEvent", [name, params]);
            } else if (params && valueToSum) {
                exec(s, f, "FacebookConnectPlugin", "logEvent", [name, params, valueToSum]);
            } else {
                f("Invalid arguments");
            }
        }
    };

    module.exports = facebookConnectPlugin;
}
