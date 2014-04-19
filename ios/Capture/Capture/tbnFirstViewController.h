//
//  tbnFirstViewController.h
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>

#define kAutoHideMillis 3000
#define kAutoHide true
#define kWebMapURL @"http://poroawards.net/Geolocation/map.html"

//----------File Location for MapURL------------//
//use "file:///android_asset/map.html" for device
//use "http://poroawards.net/Geolocation/map.html" for web or emulator, change hosting later


@interface tbnFirstViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIWebView *webView;

@end
