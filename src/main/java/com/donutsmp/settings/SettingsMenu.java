package com.donutsmp.settings;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds the main category menu and each per-category settings dialog on the fly,
 * so button labels/colors always reflect the player's current values.
 */
public final class SettingsMenu {

    private final PlayerSettingsStore store;
    private final List<SettingChangeHook> hooks;

    public SettingsMenu(PlayerSettingsStore store, SettingChangeHook... hooks) {
        this.store = store;
        this.hooks = List.of(hooks);
    }

    public void openMain(Player player) {
        player.showDialog(buildMainDialog());
    }

    private Dialog buildMainDialog() {
        List<ActionButton> buttons = new ArrayList<>();

        for (Map.Entry<String, String> entry : SettingsRegistry.CATEGORIES.entrySet()) {
            String categoryId = entry.getKey();
            String label = entry.getValue();

            buttons.add(ActionButton.builder(Component.text(label, NamedTextColor.WHITE))
                    .width(240)
                    .action(DialogAction.customClick(
                            (view, audience) -> {
                                if (audience instanceof Player p) {
                                    p.showDialog(buildCategoryDialog(p, categoryId, label));
                                }
                            },
                            ClickCallback.Options.builder().uses(-1).build()
                    ))
                    .build());
        }

        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Settings", NamedTextColor.WHITE))
                        .body(List.of(
                                io.papermc.paper.registry.data.dialog.body.DialogBody.plainMessage(
                                        Component.text("Choose a category to change your Donut SMP settings",
                                                NamedTextColor.GRAY))
                        ))
                        .build())
                .type(DialogType.multiAction(buttons).build())
        );
    }

    private Dialog buildCategoryDialog(Player player, String categoryId, String categoryLabel) {
        List<SettingDef> defs = SettingsRegistry.SETTINGS.get(categoryId);
        List<ActionButton> buttons = new ArrayList<>();

        for (SettingDef def : defs) {
            buttons.add(buildToggleButton(player, categoryId, categoryLabel, def));
        }

        buttons.add(ActionButton.builder(Component.text("Back", NamedTextColor.WHITE))
                .width(496)
                .action(DialogAction.customClick(
                        (view, audience) -> {
                            if (audience instanceof Player p) {
                                p.showDialog(buildMainDialog());
                            }
                        },
                        ClickCallback.Options.builder().uses(-1).build()
                ))
                .build());

        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Settings – " + categoryLabel, NamedTextColor.WHITE))
                        .build())
                .type(DialogType.multiAction(buttons).build())
        );
    }

    private ActionButton buildToggleButton(Player player, String categoryId, String categoryLabel, SettingDef def) {
        int index = store.get(player.getUniqueId(), def);
        SettingDef.Option current = def.options().get(index);

        Component label = Component.text(def.label() + ": ", NamedTextColor.WHITE)
                .append(Component.text(current.text(), current.color()));

        return ActionButton.builder(label)
                .width(496)
                .action(DialogAction.customClick(
                        (view, audience) -> {
                            if (audience instanceof Player p) {
                                store.cycle(p.getUniqueId(), def);
                                int newIndex = store.get(p.getUniqueId(), def);

                                if (def.hasCommand(newIndex)) {
                                    p.performCommand(def.commandFor(newIndex));
                                }

                                for (SettingChangeHook hook : hooks) {
                                    hook.onChange(p, categoryId, def.key(), newIndex);
                                }

                                p.showDialog(buildCategoryDialog(p, categoryId, categoryLabel));
                            }
                        },
                        ClickCallback.Options.builder().uses(-1).build()
                ))
                .build();
    }
}
