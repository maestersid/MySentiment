//
//  ViewController.swift
//  MobileAzureDevDays
//
//  Created by Colby Williams on 9/22/17.
//  Copyright © 2017 Colby Williams. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextViewDelegate {

	@IBOutlet weak var sentimentTextView: UITextView!
	@IBOutlet weak var sentimentTextContainer: UIView!
	@IBOutlet weak var sentimentTextPlaceholder: UILabel!
	
	@IBOutlet weak var submitButton: UIButton!
    @IBOutlet weak var crashButton: UIButton!
    @IBOutlet weak var eventButton: UIButton!
    @IBOutlet weak var colorButton: UIButton!
	
	@IBOutlet weak var activityIndicator: UIActivityIndicatorView!
	@IBOutlet weak var activityLabel: UILabel!
	
	@IBOutlet weak var emojiLabel: UILabel!
	
	
	let submit = "Submit"
	let reset = "Reset"
	
	
	override func viewDidLoad() {
		super.viewDidLoad()
		setupBorders()
        
        submitButton.isEnabled = false
        
        SentimentClient.shared.obtainKey(){keyResponse in
            if let document = keyResponse {
                SentimentClient.shared.apiKey = document.key
                SentimentClient.shared.region = document.region
            } else {
                self.showApiKeyAlert()
            }
            
            DispatchQueue.main.async { self.submitButton.isEnabled = true }
        }
	}

	
	@IBAction func submitButtonTouchUpInside(_ sender: Any) {
		
		if submitButton.currentTitle == submit {
		
			if !sentimentTextView.hasText {
				showErrorAlert("Hold up", message: "Gotta add some text first.", dismiss: "Got it"); return
			}
			
			setActivity(true)
			
			SentimentClient.shared.determineSentiment(sentimentTextView.text) { sentimentResponse in
				
				self.setActivity(false)
				
				if let response = sentimentResponse, let document = response.documents.first {
				
					self.setSentiment(Sentiment.getSentiment(document.score))
				
				} else {
					
					self.showErrorAlert("Error \(sentimentResponse?.errorStatusCode ?? -1)", message: sentimentResponse?.errorMessage ?? "", dismiss: "Okay")
				}
			}
			
		} else if submitButton.currentTitle == reset {
			
			setSentiment(nil)
			
			resetTextView()
		}
	}
    
    @IBAction func crashButtonTapped(_: UIButton) {
        presentCrashAlert()
    }
    
    // - MARK: Alert Functions
    func presentCrashAlert() {
        let alert = UIAlertController(title: "The app will close",
                                      message: "A crash report will be sent when you reopen the app.",
                                      preferredStyle: UIAlertControllerStyle.alert)
        
        // Cancel Button
        alert.addAction(UIAlertAction(title: "Cancel",
                                      style: UIAlertActionStyle.default,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
        }))
        // Crash App button
        alert.addAction(UIAlertAction(title: "Crash app",
                                      style: UIAlertActionStyle.destructive,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
                                        // generate test crash
                                        fatalError()
        }))
        
        present(alert, animated: true, completion: nil)
    }
	
	
	func showErrorAlert(_ title:String, message:String, dismiss:String) {

		let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
		
		alertController.addAction(UIAlertAction(title: dismiss, style: .default) { a in
			self.dismiss(animated: true) { }
		});
		
		present(alertController, animated: true) {}
	}
    
    @IBAction func analyticsButtonTapped(_ sender: UIButton) {
        switch sender {
        case eventButton:
            print("send a custom alert via Cocoapods")
            presentCustomEventAlert()
            
        case colorButton:
            print("custom color property button pressed")
            presentColorPropertyAlert()
            
        default:
            break
        }
    }
 
    // - MARK: Alert Functions
    
    func presentCustomEventAlert() {
        let alert = UIAlertController(title: "Event sent",
                                      message: "",
                                      preferredStyle: .alert)
        
        // OK Button
        alert.addAction(UIAlertAction(title: "OK",
                                      style: .default,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
        }))
        present(alert, animated: true, completion: nil)
    }
    
    func presentColorPropertyAlert() {
        let alert = UIAlertController(title: "Choose a color",
                                      message: "",
                                      preferredStyle: .alert)
        alert.view.tintColor = UIColor.black
        
        // Yellow button
        alert.addAction(UIAlertAction(title: "💛 Yellow",
                                      style: .default,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
        }))
        
        // Blue button
        alert.addAction(UIAlertAction(title: "💙 Blue",
                                      style: .default,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
        }))
        
        // Red button
        alert.addAction(UIAlertAction(title: "❤️ Red",
                                      style: .default,
                                      handler: { _ in alert.dismiss(animated: true, completion: nil)
        }))
        
        present(alert, animated: true, completion: nil)
    }

	
	func setSentiment(_ sentiment:Sentiment?) {
		emojiLabel.text = sentiment?.emoji
		view.backgroundColor = sentiment?.color ?? #colorLiteral(red: 1, green: 0.4352941176, blue: 0.4117647059, alpha: 1)
		submitButton.setTitle(sentiment == nil ? submit : reset, for: .normal)
	}
	
	
	func resetTextView() {
		sentimentTextView.text = nil
		sentimentTextPlaceholder.isHidden = false
		sentimentTextView.isEditable = true
	}
	
	
	func setActivity(_ activity: Bool) {
		activityLabel.isHidden = !activity
		activityIndicator.isHidden = !activity
		sentimentTextView.isEditable = !activity
		submitButton.isUserInteractionEnabled = !activity
		
		if activity && !activityIndicator.isAnimating {
			activityIndicator.startAnimating()
		}
	}
	
	
	func setupBorders() {
		sentimentTextContainer.layer.borderWidth = 1
		sentimentTextContainer.layer.borderColor = #colorLiteral(red: 0.6078431373, green: 0.6078431373, blue: 0.6078431373, alpha: 1)
		sentimentTextContainer.layer.cornerRadius = 8
		
		submitButton.layer.borderWidth = 1.5
		submitButton.layer.borderColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
	}

	
	override var preferredStatusBarStyle: UIStatusBarStyle {
		return .lightContent
	}
	
	
	// MARK - UITextViewDelegate
	
	func textViewDidChange(_ textView: UITextView) {
		sentimentTextPlaceholder.isHidden = textView.hasText
	}

    
    func showApiKeyAlert() {
        
        if SentimentClient.shared.apiKey == nil || SentimentClient.shared.apiKey!.isEmpty {
            
            let alertController = UIAlertController(title: "Configure App", message: "Enter a Text Analytics API Subscription Key. Or add the key in code in `didFinishLaunchingWithOptions`", preferredStyle: .alert)
            
            alertController.addTextField() { textField in
                textField.placeholder = "Subscription Key"
                textField.returnKeyType = .done
            }
            
            alertController.addAction(UIAlertAction(title: "Get Key", style: .default) { a in
                if let getKeyUrl = URL(string: "https://portal.azure.com/#blade/HubsExtension/BrowseResourceBlade/resourceType/Microsoft.CognitiveServices%2Faccounts") {
                    UIApplication.shared.open(getKeyUrl, options: [:]) { opened in
                        print("Opened GetKey url successfully: \(opened)")
                    }
                }
            })
            
            alertController.addAction(UIAlertAction(title: "Done", style: .default) { a in
                if alertController.textFields?.first?.text == nil || alertController.textFields!.first!.text!.isEmpty {
                    self.showApiKeyAlert()
                } else {
                    SentimentClient.shared.apiKey = alertController.textFields!.first!.text
                }
            })
            
            present(alertController, animated: true) { }
        }
    }
}
