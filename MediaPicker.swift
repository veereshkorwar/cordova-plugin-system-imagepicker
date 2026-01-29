import PhotosUI

@objc(MediaPicker) class MediaPicker: CDVPlugin, PHPickerViewControllerDelegate {
    var callbackId: String?

    @objc(pickIOS:)
    func pickIOS(command: CDVInvokedUrlCommand) {
        self.callbackId = command.callbackId

        var configuration = PHPickerConfiguration()
        configuration.filter = .any(of: [.images, .videos])
        configuration.selectionLimit = 0 // unlimited or use args if desired

        let picker = PHPickerViewController(configuration: configuration)
        picker.delegate = self

        self.viewController.present(picker, animated: true, completion: nil)
    }

    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true, completion: nil)
        var uris: [String] = []

        for result in results {
            if let provider = result.itemProvider.registeredTypeIdentifiers.first {
                uris.append(result.itemProvider.suggestedName ?? provider)
            }
        }

        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: uris)
        self.commandDelegate.send(pluginResult, callbackId: self.callbackId)
    }
}
