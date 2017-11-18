@REM Download latest NUR API libraries from GitHub.

@SET GETTER=wget.exe
@SET SDK_REPO=nur_sdk
@SET REMOTE_BASE=https://github.com/NordicID/%SDK_REPO%/raw/master
@SET LOCAL_IMPORTDIR=import

md import

%GETTER% -q %REMOTE_BASE%/java/ReleaseNotes.txt -O %LOCAL_IMPORTDIR%\ReleaseNotes.txt
%GETTER% -q %REMOTE_BASE%/java/NurApi.jar -O %LOCAL_IMPORTDIR%\NurApi.jar

@SET GETTER=
@SET REMOTE_DIR=
@REM @PAUSE