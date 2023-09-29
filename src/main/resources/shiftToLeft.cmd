# Get the screen width and height
SCREEN_WIDTH=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $3}')
SCREEN_HEIGHT=$(xdpyinfo | awk -F'[ x]+' '/dimensions:/{print $4}')

# Calculate the new width (half of the screen width) and height (full screen height)
NEW_WIDTH=$((SCREEN_WIDTH / 2))
NEW_HEIGHT=$SCREEN_HEIGHT

# Move and resize the Mattermost Desktop window to the upper left corner of the screen
wmctrl -r "Mattermost Desktop" -e 0,0,0,$NEW_WIDTH,$NEW_HEIGHT
