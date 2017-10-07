var videoLoader = {

    loadVideo: function(url, token, successCallback, errorCallback) {
        cordova.exec(successCallback,
                    errorCallback,
                    'VideoLoader', 'loadVideo',
                    [url, token]
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
