#!/bin/bash

# Phonemics Android Keyboard - Build and Test Script

echo "🚀 Building Phonemics Android Keyboard..."

# Check if Android SDK is available
if ! command -v adb &> /dev/null; then
    echo "❌ Android SDK not found. Please install Android Studio and set up SDK."
    exit 1
fi

# Build the project
echo "📦 Building APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    
    # Check if device is connected
    if adb devices | grep -q "device$"; then
        echo "📱 Device detected. Installing APK..."
        adb install -r app/build/outputs/apk/debug/app-debug.apk
        
        if [ $? -eq 0 ]; then
            echo "✅ Installation successful!"
            echo ""
            echo "🎉 Next steps:"
            echo "1. Open the 'Phonemics Keyboard' app on your device"
            echo "2. Follow the setup instructions to enable the keyboard"
            echo "3. Test the keyboard using the test screen"
            echo ""
            echo "🔧 Manual setup:"
            echo "• Go to Settings > System > Languages & input > Virtual keyboard"
            echo "• Enable 'Phonemics Arabic Keyboard'"
            echo "• Select it as your active keyboard"
        else
            echo "❌ Installation failed"
        fi
    else
        echo "⚠️  No device connected. APK built successfully at:"
        echo "   app/build/outputs/apk/debug/app-debug.apk"
        echo ""
        echo "To install manually:"
        echo "1. Connect your Android device"
        echo "2. Enable USB debugging"
        echo "3. Run: adb install -r app/build/outputs/apk/debug/app-debug.apk"
    fi
else
    echo "❌ Build failed. Please check the errors above."
fi