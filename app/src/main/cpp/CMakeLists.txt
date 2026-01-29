cmake_minimum_required(VERSION 3.22.1)
project(llama_jni)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(LLAMA_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_SERVER OFF CACHE BOOL "" FORCE)
set(LLAMA_CURL OFF CACHE BOOL "" FORCE)
set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)

# Optional, falls du common utilities brauchst
set(LLAMA_BUILD_COMMON ON CACHE BOOL "" FORCE)

add_subdirectory(llama.cpp)

add_library(llama_jni SHARED
        llama_jni.cpp
)

find_library(log-lib log)

target_include_directories(llama_jni PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/llama.cpp/include
)

# Minimum: llama
# Optional: common, wenn du später common.h nutzt
target_link_libraries(llama_jni
        llama
        ${log-lib}
)
