cmake_minimum_required(VERSION 3.22.1)
project(llama_jni)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(LLAMA_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_SERVER OFF CACHE BOOL "" FORCE)
set(LLAMA_CURL OFF CACHE BOOL "" FORCE)
set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)

# Optional, aber kann Build Zeit sparen
set(LLAMA_BUILD_COMMON OFF CACHE BOOL "" FORCE)

add_subdirectory(llama.cpp)

add_library(llama_jni SHARED
        llama_jni.cpp
)

find_library(log-lib log)

target_include_directories(llama_jni PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/llama.cpp/include
)

target_link_libraries(llama_jni
        llama
        ${log-lib}
)

# Optimierungsflags fuer beide Targets: llama und llama_jni
# Android Studio nutzt pro Variant eigene Build Types, Release ist der entscheidende.
if (CMAKE_BUILD_TYPE STREQUAL "Release")
    target_compile_options(llama PRIVATE
            -O3
            -DNDEBUG
            -ffast-math
    )
    target_compile_options(llama_jni PRIVATE
            -O3
            -DNDEBUG
            -ffast-math
    )
elseif (CMAKE_BUILD_TYPE STREQUAL "Debug")
    # Optional: Debug nicht katastrophal langsam machen
    target_compile_options(llama PRIVATE
            -O2
    )
    target_compile_options(llama_jni PRIVATE
            -O2
    )
endif()
