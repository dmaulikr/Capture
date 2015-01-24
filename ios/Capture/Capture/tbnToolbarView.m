//
//  tbnToolbarView.m
//  Capture
//
//  Created by Sacha Best on 4/21/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnToolbarView.h"

@implementation tbnToolbarView

+ (tbnToolbarView *)create {
    tbnToolbarView *toReturn = [[tbnToolbarView alloc] initWithFrame:CGRectMake(0, [UIScreen mainScreen].bounds.size.height - kImageSize - kTabBarHeight, [UIScreen mainScreen].bounds.size.width, kImageSize)];
    toReturn.backgroundColor = [UIColor clearColor];
    toReturn.opaque = NO;
    toReturn.userImage = [[PFImageView alloc] initWithFrame:CGRectMake(0, 0, kImageSize, kImageSize)];
    toReturn.userImage.file = [tbnParseManager getUserPhotoFetched];
    [toReturn.userImage loadInBackground:^(UIImage *image, NSError *error) {
        toReturn.userImage.frame = CGRectMake(toReturn.userImage.frame.origin.x, toReturn.userImage.frame.origin.y, kImageSize, kImageSize);
    }];
    [toReturn addSubview:toReturn.userImage];
    toReturn.name = [[UILabel alloc] initWithFrame:CGRectMake(kImageSize + 10, 5, 75, 22)];
    toReturn.name.text = [tbnParseManager getCurrentUser].username;
    [toReturn addSubview:toReturn.name];
    toReturn.coin = [[UILabel alloc] initWithFrame:CGRectMake(toReturn.frame.size.width - 2 * kImageSize, 0, kImageSize, kImageSize)];
    toReturn.coin.textAlignment = NSTextAlignmentCenter;
    toReturn.coin.font = [UIFont systemFontOfSize:20];
    toReturn.coin.text = [[[tbnParseManager getCurrentUser] objectForKey:@"gold"] description];
    toReturn.coin.backgroundColor = [UIColor colorWithRed:255/255.0f green:234/255.0f blue:19/255.0f alpha:0.5f];
    [toReturn addSubview:toReturn.coin];
    toReturn.army = [[UILabel alloc] initWithFrame:CGRectMake(toReturn.frame.size.width - kImageSize, 0, kImageSize, kImageSize)];
    toReturn.army.textAlignment = NSTextAlignmentCenter;
    toReturn.army.font = [UIFont systemFontOfSize:20];
    toReturn.army.text = [[[tbnParseManager getCurrentUser] objectForKey:@"army"] description];
    toReturn.army.backgroundColor = [UIColor colorWithRed:91/255.0f green:224/255.0f blue:245/255.0f alpha:0.5f];
    [toReturn addSubview:toReturn.army];
    return toReturn;
}

@end
