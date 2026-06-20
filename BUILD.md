# ❄ FrostVisuals — Компиляция

## Требования
- **Java 17** (рекомендуется) или Java 8
- Подключение к интернету (при первом билде скачает ~300MB Forge+MC)

## Скачать Java 17
https://adoptium.net/temurin/releases/?version=17

## Компиляция (1 команда)

### Windows:
```
gradlew.bat build
```

### Linux / macOS:
```bash
chmod +x gradlew
./gradlew build
```

## Результат

После успешной компиляции файл появится в:
```
build/libs/FrostVisuals-1.0.0.jar
```

## Установка в Minecraft

1. Установи **Forge 1.16.5** (https://files.minecraftforge.net/)
2. Скопируй `FrostVisuals-1.0.0.jar` в папку `.minecraft/mods/`
3. Запускай Minecraft через Forge профиль
4. В игре: **Right Shift** → открывается меню ❄ FrostVisuals

## Управление в игре

| Клавиша | Действие |
|---|---|
| Right Shift | Открыть главное меню |
| C | Zoom |
| Alt | Freelook |
| Tab (в редакторе меча) | Переключить режим MOVE/ROTATE/SCALE |
| Стрелки (в редакторе) | Двигать/вращать позицию меча |
| Enter (в редакторе) | Сохранить |
| Esc (в редакторе) | Отмена |

## Если первый билд долгий

Это нормально — первый раз Gradle качает Minecraft + Forge (~300MB).
Повторные компиляции занимают ~15 секунд.

## Возможные ошибки

**"JAVA_HOME is set to an invalid directory"**
→ Укажи путь к JDK в JAVA_HOME или используй sdkman:
```bash
sdk install java 17-tem
```

**"Permission denied" на gradlew**
```bash
chmod +x gradlew
```
