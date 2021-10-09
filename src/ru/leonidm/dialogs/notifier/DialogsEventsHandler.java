package ru.leonidm.dialogs.notifier;

import java.util.HashMap;
import java.util.ArrayList;
import ru.leonidm.dialogs.api.DialogsAPI;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.HoverEvent;
import ru.leonidm.dialogs.api.events.QuestLoadEvent;
import ru.worldm.library.UtilsM;
import ru.leonidm.dialogs.api.events.NPCLoadEvent;
import ru.leonidm.dialogs.api.events.QuestsPreReloadEvent;
import ru.leonidm.dialogs.api.events.NPCsPreReloadEvent;
import org.bukkit.event.EventHandler;
import ru.leonidm.dialogs.api.events.DialogsConfigReloadEvent;
import java.util.UUID;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.List;
import org.bukkit.entity.Player;
import java.util.Map;
import org.bukkit.event.Listener;

public class DialogsEventsHandler implements Listener
{
    private static final Map<Player, List<String>> shownQuests = new HashMap<>();
    private static final Map<String, TextComponent> questsToDisplayName = new HashMap<>();
    private static final Map<String, String> questsToNpc = new HashMap<>();
    private static final Map<UUID, String> npcs = new HashMap<>();

    @EventHandler
    public void onConfigReload(DialogsConfigReloadEvent e) {
        Messages.reload();
    }
    
    @EventHandler
    public void onNPCsPreReload(NPCsPreReloadEvent e) {
        DialogsEventsHandler.npcs.clear();
    }
    
    @EventHandler
    public void onQuestsPreReload(QuestsPreReloadEvent e) {
        DialogsEventsHandler.questsToDisplayName.clear();
        DialogsEventsHandler.questsToNpc.clear();
    }
    
    @EventHandler
    public void onNPCLoad(NPCLoadEvent e) {
        DialogsEventsHandler.npcs.put(e.getNpcUuid(), UtilsM.colorize((String)e.getMap().get("name")));
    }
    
    @EventHandler
    public void onQuestLoad(QuestLoadEvent e) {
        String displayName = UtilsM.colorize((String)e.getMap().get("displayName"));
        String info = UtilsM.colorize((String)e.getMap().get("info"));
        TextComponent out = new TextComponent(displayName);
        out.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(info)));
        DialogsEventsHandler.questsToDisplayName.put(e.getQuestName(), out);
        DialogsEventsHandler.questsToNpc.put(e.getQuestName(),
                DialogsEventsHandler.npcs.get(UUID.fromString((String)e.getMap().get("uuid"))));
    }
    
    public static void checkQuests(Player player) {
        List<String> quests = DialogsAPI.getQuestsInProgressNames(player, null);
        if(quests == null) {
            return;
        }
        List<String> shownQuests = DialogsEventsHandler.shownQuests.computeIfAbsent(player, k -> new ArrayList<>());
        for(String questName : quests) {
            if(!shownQuests.contains(questName) && DialogsAPI.canQuestBeCompleted(player, questName)) {
                shownQuests.add(questName);
                player.spigot().sendMessage(generateComponent(questName));
            }
        }
    }
    
    private static TextComponent generateComponent(String questName) {
        String npcName = DialogsEventsHandler.questsToNpc.get(questName);
        TextComponent out = new TextComponent(Messages.QUEST_CAN_BE_COMPLETED_FIRST.replace("%npc", npcName));
        out.addExtra(DialogsEventsHandler.questsToDisplayName.get(questName));
        out.addExtra(Messages.QUEST_CAN_BE_COMPLETED_LAST.replace("%npc", npcName));
        return out;
    }
    
    public static void remove(Player player) {
        DialogsEventsHandler.shownQuests.remove(player);
    }
}
