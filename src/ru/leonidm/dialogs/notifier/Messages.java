package ru.leonidm.dialogs.notifier;

import org.bukkit.configuration.file.FileConfiguration;
import ru.worldm.library.UtilsM;

public class Messages
{
    public static String QUEST_CAN_BE_COMPLETED_FIRST;
    public static String QUEST_CAN_BE_COMPLETED_LAST;

    static {
        reload();
    }

    public static void reload() {
        QuestsNotifier.getInstance().reloadConfig();
        FileConfiguration config = QuestsNotifier.getInstance().getConfig();
        String questCanBeCompleted = UtilsM.colorize(config.getString("quest_can_be_completed"));
        String[] split = questCanBeCompleted.split("%quest", 2);
        if (questCanBeCompleted.startsWith("%quest")) {
            Messages.QUEST_CAN_BE_COMPLETED_FIRST = "";
            Messages.QUEST_CAN_BE_COMPLETED_LAST = split[1];
        }
        else if (questCanBeCompleted.endsWith("%quest")) {
            Messages.QUEST_CAN_BE_COMPLETED_FIRST = split[0];
            Messages.QUEST_CAN_BE_COMPLETED_LAST = "";
        }
        else if (!questCanBeCompleted.contains("%quest")) {
            Messages.QUEST_CAN_BE_COMPLETED_FIRST = questCanBeCompleted;
            Messages.QUEST_CAN_BE_COMPLETED_LAST = "";
        }
        else {
            Messages.QUEST_CAN_BE_COMPLETED_FIRST = split[0];
            Messages.QUEST_CAN_BE_COMPLETED_LAST = split[1];
        }
    }
}
