const exec = require('cordova/exec');

const systemImagePicker = {
  getPictures: function(successCallback, errorCallback, options) {
    var opts = options || {};
    var maximumImagesCount = opts.maximumImagesCount || 10;

    var pickOptions = {
      multiple: maximumImagesCount,
      mediaTypes: ['image', 'video']
    };

    if (cordova.platformId === 'android') {
      exec(successCallback, errorCallback, 'MediaPicker', 'pickAndroid', [pickOptions]);
    } else {
      errorCallback('Unsupported platform');
    }
  }
};

module.exports = systemImagePicker;
