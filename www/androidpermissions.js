var exec = cordova.require('cordova/exec');
module.exports = {
    androidPermissions: function(success, error, args) { 
        console.log('androidPermissions');    
        exec(success, error, "AndroidPermissions", "androidPermissions", []);
    }
};

