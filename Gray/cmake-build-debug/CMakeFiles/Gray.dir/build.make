# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.17

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "C:\Programs\JetBrains\CLion 2020.2.1\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "C:\Programs\JetBrains\CLion 2020.2.1\bin\cmake\win\bin\cmake.exe" -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = C:\Users\enxy0\IdeaProjects\kek\Gray

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Gray.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Gray.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Gray.dir/flags.make

CMakeFiles/Gray.dir/main.cpp.obj: CMakeFiles/Gray.dir/flags.make
CMakeFiles/Gray.dir/main.cpp.obj: ../main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Gray.dir/main.cpp.obj"
	C:\MinGW\bin\g++.exe  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles\Gray.dir\main.cpp.obj -c C:\Users\enxy0\IdeaProjects\kek\Gray\main.cpp

CMakeFiles/Gray.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Gray.dir/main.cpp.i"
	C:\MinGW\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\enxy0\IdeaProjects\kek\Gray\main.cpp > CMakeFiles\Gray.dir\main.cpp.i

CMakeFiles/Gray.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Gray.dir/main.cpp.s"
	C:\MinGW\bin\g++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\enxy0\IdeaProjects\kek\Gray\main.cpp -o CMakeFiles\Gray.dir\main.cpp.s

# Object files for target Gray
Gray_OBJECTS = \
"CMakeFiles/Gray.dir/main.cpp.obj"

# External object files for target Gray
Gray_EXTERNAL_OBJECTS =

Gray.exe: CMakeFiles/Gray.dir/main.cpp.obj
Gray.exe: CMakeFiles/Gray.dir/build.make
Gray.exe: CMakeFiles/Gray.dir/linklibs.rsp
Gray.exe: CMakeFiles/Gray.dir/objects1.rsp
Gray.exe: CMakeFiles/Gray.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable Gray.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\Gray.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Gray.dir/build: Gray.exe

.PHONY : CMakeFiles/Gray.dir/build

CMakeFiles/Gray.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\Gray.dir\cmake_clean.cmake
.PHONY : CMakeFiles/Gray.dir/clean

CMakeFiles/Gray.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" C:\Users\enxy0\IdeaProjects\kek\Gray C:\Users\enxy0\IdeaProjects\kek\Gray C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug C:\Users\enxy0\IdeaProjects\kek\Gray\cmake-build-debug\CMakeFiles\Gray.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Gray.dir/depend

