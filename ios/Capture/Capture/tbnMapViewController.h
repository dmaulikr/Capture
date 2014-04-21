//
//  tbnFirstViewController.h
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "tbnCaptureWebView.h"

#define kAutoHideMillis 3000
#define kAutoHide true

//----------File Location for MapURL------------//
//use "file:///android_asset/map.html" for device
//use "http://poroawards.net/Geolocation/map.html" for web or emulator, change hosting later


@interface tbnMapViewController : UIViewController <PFLogInViewControllerDelegate>

@property (weak, nonatomic) IBOutlet tbnCaptureWebView *webView;

- (void) showLoginWindow;
- (void) showTooltop:(PFObject *)capturePoint x:(float)x y:(float)y;

@end
