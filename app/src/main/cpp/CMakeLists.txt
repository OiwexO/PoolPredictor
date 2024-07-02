# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.
project("poolpredictor")

# Suppresses Format string is not a string literal (potentially insecure) warning
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-format-security")

# used for mocking data in debug builds
if (${CMAKE_BUILD_TYPE} MATCHES "Debug")
endif()
add_definitions(-DBUILD_TYPE_DEBUG)

# Enables/disables logs
add_definitions(-DENABLE_LOGS)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
add_library(
        # Sets the name of the library.
        poolpredictor

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        bridge/NativeBridge.cpp

        mod/data/table/TableProperties.cpp

        mod/data/type/Point2D.cpp

        mod/data/GlobalSettings.cpp

        mod/memory/MemoryManager.cpp
        mod/memory/inner/Balls.cpp
        mod/memory/inner/CueProperties.cpp
        mod/memory/inner/GameManager.cpp
        mod/memory/inner/MenuManager.cpp
        mod/memory/inner/UserSettings.cpp
        mod/memory/inner/VisualCue.cpp

        mod/predictor/Prediction.cpp

        native-lib.cpp
)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
find_library(
        # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log
)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries(
        # Specifies the target library.
        poolpredictor

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
)
