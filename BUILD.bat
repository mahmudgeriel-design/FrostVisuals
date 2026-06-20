@echo off
title FrostVisuals - Сборка мода
color 0B
echo.
echo  ====================================
echo   FrostVisuals - Сборка мода
echo  ====================================
echo.

:: Проверяем Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo  [ОШИБКА] Java не найдена!
    echo.
    echo  Скачай Java 17 БЕСПЛАТНО здесь:
    echo  https://adoptium.net
    echo  (Нажми "Latest LTS Release")
    echo.
    pause
    start https://adoptium.net
    exit /b 1
)

echo  [OK] Java найдена!
echo.
echo  Начинаю сборку... (первый раз 5-10 минут)
echo  Не закрывай это окно!
echo.

call gradlew.bat build --no-daemon

if %ERRORLEVEL% EQU 0 (
    echo.
    echo  ====================================
    echo   ГОТОВО! Мод собран успешно!
    echo  ====================================
    echo.
    echo  Файл мода: build\libs\FrostVisuals-1.0.jar
    echo.
    echo  Копирую в папку mods...

    set MODS_DIR=%APPDATA%\.minecraft\mods
    if not exist "%MODS_DIR%" mkdir "%MODS_DIR%"
    copy /Y "build\libs\FrostVisuals-1.0.jar" "%MODS_DIR%\FrostVisuals-1.0.jar"

    if %ERRORLEVEL% EQU 0 (
        echo  [OK] Скопирован в %MODS_DIR%
        echo.
        echo  Готово! Запускай Minecraft с Forge 1.16.5
        echo  В игре: правый Shift = меню FrostVisuals
    ) else (
        echo  Скопируй вручную:
        echo  build\libs\FrostVisuals-1.0.jar  -^>  %APPDATA%\.minecraft\mods\
    )
    echo.
    explorer "build\libs"
) else (
    echo.
    echo  [ОШИБКА] Сборка не удалась.
    echo  Убедись что Java 17 установлена правильно.
    echo  https://adoptium.net
)

pause
