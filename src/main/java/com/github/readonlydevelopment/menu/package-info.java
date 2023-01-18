/*
 * This file is part of JDATools, licensed under the MIT License (MIT).
 *
 * Copyright (c) ROMVoid95
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Menus package.<br>
 * Contains the {@link com.github.readonlydevelopment.menu.Menu Menu} class and all
 * standard implementations of it:
 * <ul>
 * <li>{@link com.github.readonlydevelopment.menu.ButtonMenu ButtonMenu}
 * <br>
 * A menu where users select a choice via "reaction-buttons".</li>
 *
 * <li>{@link com.github.readonlydevelopment.menu.OrderedMenu OrderedMenu}
 * <br>
 * A menu with 1 - 10 ordered items, each with their own reaction to choose them
 * with.</li>
 *
 * <li>{@link com.github.readonlydevelopment.menu.Paginator Paginator}
 * <br>
 * A menu that paginates a number of items across a number of pages and uses
 * reactions to traverse between them.</li>
 *
 * <li>{@link com.github.readonlydevelopment.menu.SelectionDialog SelectionDialog}
 * <br>
 * A menu that orders choices and uses a indicator and reactions to choose one
 * of the choices.</li>
 *
 * <li>{@link com.github.readonlydevelopment.menu.Slideshow Slideshow}
 * <br>
 * A menu similar to the Paginator that displays a picture on each page.</li>
 * </ul>
 *
 * All menus also come with an implementation of a
 * {@link com.github.readonlydevelopment.menu.Menu.Builder Menu.Builder}
 * as a static inner class of the corresponding Menu implementation, which are
 * the main entryway to create said
 * implementations for usage.
 *
 * <p>
 * Please note that this entire package makes <b>HEAVY</b> usage of the
 * {@link com.github.readonlydevelopment.common.waiter.EventWaiter EventWaiter}.
 */
package com.github.readonlydevelopment.menu;
