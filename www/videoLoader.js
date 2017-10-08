var videoLoader = {

    loadVideo: function(url, token,lap, successCallback, errorCallback) {
        cordova.exec(successCallback,
                    errorCallback,
                    'VideoLoader', 'loadVideo',
                    [url, token,lap]
        );
    },
    clear: function(successCallback, errorCallback) {
        cordova.exec(successCallback,
                    errorCallback,
                    'VideoLoader', 'clearStorage',
                    []
        );
    }
};
module.exports = videoLoader;
