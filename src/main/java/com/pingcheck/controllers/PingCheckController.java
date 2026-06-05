/*
 * Copyright (c) 2026, Jaesic Olguin <jaesic.olguin@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.pingcheck.controllers;

import com.pingcheck.PingCheckConfig;
import com.pingcheck.alerts.AlertScreenFlashOverlay;
import com.pingcheck.alerts.AlertTextOverlay;
import java.awt.Color;
import lombok.Setter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;

public abstract class PingCheckController {
    @Setter
    private int ping;

    @Inject
    private PingCheckConfig config;

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private AlertScreenFlashOverlay screenFlashOverlay;

    @Inject
    private AlertTextOverlay alertTextOverlay;

    public boolean isHighPing() {
        return ping > config.pingThreshold();
    }

    /**
     * Gets set of menu options to be hidden if ping is too high
     */
    abstract protected String[] getHiddenMenuOptions();

    protected void addChatMessage() {
        clientThread.invokeLater(() -> {
            String highlightedMessage = new ChatMessageBuilder()
                .append(
                    Color.RED,
                    "Ping Check! Your ping is higher than you would prefer for this content. Go to a different world or configure your Ping Check settings differently."
                )
                .append(ChatColorType.HIGHLIGHT)
                .build();

            chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.GAMEMESSAGE)
                .runeLiteFormattedMessage(highlightedMessage)
                .build());
        });
    }
    public void alert() {
        addChatMessage();
        handleTextAlert();
        handleScreenFlashAlert();

    }

    private void handleTextAlert() {
        if (config.isTextBoxEnabled()) {
            overlayManager.add(alertTextOverlay);
        } else {
            overlayManager.remove(alertTextOverlay);
        }
    }

    private void handleScreenFlashAlert() {
        if (config.isScreenFlashingEnabled()) {
            overlayManager.add(screenFlashOverlay);
            screenFlashOverlay.startFlash();
        } else {
            overlayManager.remove(screenFlashOverlay);
        }
    }

    public void hideMenuEntries() {
        HashSet<String> options = new HashSet<>(Arrays.asList(getHiddenMenuOptions()));
        final MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries == null)
        {
            return;
        }

        final MenuEntry[] filteredEntries = Arrays.stream(menuEntries)
            .filter(x -> x != null && x.getOption() != null && !options.contains(x.getOption()))
            .toArray(MenuEntry[]::new);

        client.setMenuEntries(filteredEntries);
    }
}
