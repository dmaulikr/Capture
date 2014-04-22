//
//  tbnToolbarView.h
//  Capture
//
//  Created by Sacha Best on 4/21/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>

#define kCoinRed 255
#define kCoinGreen 255
#define kCoinBlue 0

#define kArmyRed 0
#define kArmyGreen 0
#define kArmyBlue 255

#define kTabBarHeight 49
#define kImageSize 75

@interface tbnToolbarView : UIView

@property PFImageView *userImage;
@property UILabel *army;
@property UILabel *coin;
@property UILabel *name;

+ (tbnToolbarView *)create;

@end
