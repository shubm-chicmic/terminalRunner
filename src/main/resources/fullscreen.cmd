#!/bin/bash

# Get the screen width and height
SCREEN_WIDTH=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $3}')
SCREEN_HEIGHT=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $4}')

# Get the height of the taskbar (adjust this value if needed)
TASKBAR_HEIGHT=30

# Calculate the new width (full screen width) and height (screen height - taskbar height)
NEW_WIDTH=$SCREEN_WIDTH
NEW_HEIGHT=$((SCREEN_HEIGHT - TASKBAR_HEIGHT))

# Move and resize the Mattermost Desktop window to fill the screen area excluding taskbar
wmctrl -r "Mattermost Desktop" -e 0,0,$TASKBAR_HEIGHT,$NEW_WIDTH,$NEW_HEIGHT
