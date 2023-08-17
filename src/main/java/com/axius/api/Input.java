package com.axius.api;

/**
 * Contains classes related to handling collections and data structures.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Input class provides functionality for handling keyboard and key binding events.
 */
public class Input {
    private static Map<Integer, KeyboardEvent> keyInputs;
    private static ArrayList<KeyBindingEvent> bindedInputs;

    /**
     * Initializes a new Input instance with empty keyInputs and bindedInputs collections.
     */
    public Input() {
        keyInputs = new HashMap<Integer, KeyboardEvent>();
        bindedInputs = new ArrayList<KeyBindingEvent>();
    }

    /**
     * Represents a listener interface for handling key state changes.
     */
    public interface IState {
        /**
         * Called when the state of a key changes.
         *
         * @param state The new state of the key.
         */
        void listen(Type state);
    }

    /**
     * Represents different types of key states.
     */
    public enum Type {
        Default, // Default state
        Press,   // Key is pressed
        Hold,    // Key is held down
        Release  // Key is released
    }

    /**
     * Represents a keyboard event with associated key, state, and listener.
     */
    public static class KeyboardEvent {
        final String name;
        int key;
        Type state;
        final Type type;
        final IState listener;
        Boolean binded = false;

        /**
         * Initializes a new KeyboardEvent instance with the given key, state, and listener.
         *
         * @param name     The name for the keu event.
         * @param key      The key code for the event.
         * @param state    The initial state of the key.
         * @param listener The listener to be notified of state changes.
         */
        public KeyboardEvent(String name, int key, Type state, IState listener) {
            this.name = name;
            this.key = key;
            this.type = state;
            this.listener = listener;
            this.state = Type.Release;

            // Create a new event map for the given key if it doesn't exist
            keyInputs.putIfAbsent(key, this);
        }

        /**
         * Initializes a new KeyboardEvent instance with the given key and listener.
         *
         * @param name     The name for the keu event.
         * @param key      The key code for the event.
         * @param listener The listener to be notified of state changes.
         */
        public KeyboardEvent(String name, int key, IState listener) {
            this.name = name;
            this.key = key;
            this.listener = listener;
            this.type = Type.Default;
            this.state = Type.Release;

            // Create a new event map for the given key if it doesn't exist
            keyInputs.putIfAbsent(key, this);
        }

        /**
         * Initializes a new KeyboardEvent instance with the given key.
         *
         * @param name     The name for the keu event.
         * @param key      The key code for the event.
         */
        public KeyboardEvent(String name, int key) {
            this.name = name;
            this.key = key;
            this.listener = null;
            this.type = Type.Default;
            this.state = Type.Release;

            // Create a new event map for the given key if it doesn't exist
            keyInputs.putIfAbsent(key, this);
        }

        /**
         * Returns the key code associated with this keyboard event.
         *
         * @return The key code.
         */
        public int getKey() {
            return this.key;
        }

        /**
         * Returns the current state of the key.
         *
         * @return The current state.
         */
        public Type getState() {
            return this.state;
        }

        /**
         * Sets the state of the key to the provided state and notifies the listener.
         *
         * @param newState The new state to set.
         */
        public void setState(Type newState) {
            if (!this.getState().equals(newState)) {
                // Only updates the state if it wasn't already that state.

                this.state = newState;

                if ((this.getState().equals(this.type) || this.type.equals(Type.Default)) && this.listener != null) {
                    // Sends the state updates to the listener and only if it isn't the Hold state.

                    this.listener.listen(this.getState());
                }
            }
        }

        /**
         * Updates the KeyboardEvent instance with the new given key.
         *
         * @param key      The key code for the event.
         */
        public void setKey(int key) {
            this.key = key;
        }

        public boolean isBinded() {
            return this.binded;
        }

        /**
         * Checks if the key is currently being pressed.
         *
         * @return true if the key is pressed, false otherwise.
         */
        public boolean isPressing() {
            return this.getState().equals(Type.Press);
        }

        /**
         * Checks if the key is currently released.
         *
         * @return true if the key is released, false otherwise.
         */
        public boolean isReleased() {
            return this.getState().equals(Type.Release);
        }

        /**
         * Checks if the key is currently being held down.
         *
         * @return true if the key is held down, false otherwise.
         */
        public boolean isHolding() {
            return this.getState().equals(Type.Hold);
        }
    }

    /**
     * Represents a key binding event with primary and secondary keys, state, and listener.
     */
    public static class KeyBindingEvent {
        final ArrayList<KeyboardEvent> keys;
        Type state;
        final Type type;
        final IState listener;

        /**
         * Initializes a new KeyBindingEvent instance with primary and secondary keys, state, and listener.
         *
         * @param primaryKey   The primary key code for the event.
         * @param secondaryKey The secondary key code for the event.
         * @param state        The initial state of the key binding.
         * @param listener     The listener to be notified of state changes.
         */
        public KeyBindingEvent(KeyboardEvent primaryKey, KeyboardEvent secondaryKey, Type state, IState listener) {
            this.keys = new ArrayList<KeyboardEvent>();
            this.keys.add(primaryKey);
            this.keys.add(secondaryKey);

            this.state = Type.Release;
            this.listener = listener;
            this.type = state;

            primaryKey.binded = true;
            secondaryKey.binded = true;
            bindedInputs.add(this);
        }

        /**
         * Initializes a new KeyBindingEvent instance with primary and secondary keys, and listener.
         *
         * @param primaryKey   The primary key code for the event.
         * @param secondaryKey The secondary key code for the event.
         * @param listener     The listener to be notified of state changes.
         */
        public KeyBindingEvent(KeyboardEvent primaryKey, KeyboardEvent secondaryKey, IState listener) {
            this.keys = new ArrayList<KeyboardEvent>();
            this.keys.add(primaryKey);
            this.keys.add(secondaryKey);

            this.listener = listener;
            this.state = Type.Release;
            this.type = Type.Default;

            primaryKey.binded = true;
            secondaryKey.binded = true;
            bindedInputs.add(this);
        }

        /**
         * Initializes a new KeyBindingEvent instance with primary and secondary keys.
         *
         * @param primaryKey   The primary key code for the event.
         * @param secondaryKey The secondary key code for the event.
         */
        public KeyBindingEvent(KeyboardEvent primaryKey, KeyboardEvent secondaryKey) {
            this.keys = new ArrayList<KeyboardEvent>();
            this.keys.add(primaryKey);
            this.keys.add(secondaryKey);

            this.listener = null;
            this.state = Type.Release;
            this.type = Type.Default;

            primaryKey.binded = true;
            secondaryKey.binded = true;
            bindedInputs.add(this);
        }

        /**
         * Returns a map of primary and secondary keys associated with this key binding event.
         *
         * @return A map of primary and secondary keys.
         */
        public ArrayList<KeyboardEvent> getKeys() {
            return this.keys;
        }

        /**
         * Returns the current state of the key binding.
         *
         * @return The current state.
         */
        public Type getState() {
            return this.state;
        }

        /**
         * Sets the state of the key binding to the provided state and notifies the listener.
         *
         * @param newState The new state to set.
         */
        public void setState(Type newState) {
            if (!this.getState().equals(newState)) {
                // Only updates the state if it wasn't already that state.

                this.state = newState;

                if ((this.getState().equals(this.type) || this.type.equals(Type.Default))  && this.listener != null) {
                    // Sends the state updates to the listener and only if it isn't the Hold state.

                    this.listener.listen(this.getState());
                }
            }
        }


        /**
         * Checks if both the primary and secondary keys are currently being pressed.
         *
         * @return true if both keys are pressed, false otherwise.
         */
        public boolean isPressing() {
            ArrayList<KeyboardEvent> Keys = this.getKeys();
            KeyboardEvent primaryKey = Keys.get(0);
            KeyboardEvent secondaryKey = Keys.get(1);

            return (primaryKey.isPressing() && secondaryKey.isPressing());
        }

        /**
         * Checks if both the primary and secondary keys are currently released.
         *
         * @return true if both keys are released, false otherwise.
         */
        public boolean isReleased() {
            ArrayList<KeyboardEvent> Keys = this.getKeys();
            KeyboardEvent primaryKey = Keys.get(0);
            KeyboardEvent secondaryKey = Keys.get(1);

            return (primaryKey.isReleased() && secondaryKey.isReleased());
        }

        /**
         * Checks if both the primary and secondary keys are currently being held down.
         *
         * @return true if both keys are held down, false otherwise.
         */
        public boolean isHolding() {
            ArrayList<KeyboardEvent> Keys = this.getKeys();
            KeyboardEvent primaryKey = Keys.get(0);
            KeyboardEvent secondaryKey = Keys.get(1);

            return (primaryKey.isHolding() && secondaryKey.isHolding());
        }
    }

    /**
     * Returns the map of key inputs.
     *
     * @return The map of key inputs.
     */
    public static  Map<Integer, KeyboardEvent> getKeyInputs() {
        return (Map<Integer, KeyboardEvent>) keyInputs;
    }

    /**
     * Returns the list of binded input events.
     *
     * @return The list of binded input events.
     */
    public static ArrayList<KeyBindingEvent> getbindedInputs() {
        return (ArrayList<KeyBindingEvent>) bindedInputs;
    }

    /**
     * The Key class contains constants for various keyboard key codes.
     */
    public static final class Key {

        /** The unknown key. */
        public static final int KEY_UNKNOWN = -1;

        /** Printable keys. */
        public static final int
                KEY_SPACE = 32,
                KEY_APOSTROPHE = 39,
                KEY_COMMA = 44,
                KEY_MINUS = 45,
                KEY_PERIOD = 46,
                KEY_SLASH = 47,
                KEY_0 = 48,
                KEY_1 = 49,
                KEY_2 = 50,
                KEY_3 = 51,
                KEY_4 = 52,
                KEY_5 = 53,
                KEY_6 = 54,
                KEY_7 = 55,
                KEY_8 = 56,
                KEY_9 = 57,
                KEY_SEMICOLON = 59,
                KEY_EQUAL = 61,
                KEY_A = 65,
                KEY_B = 66,
                KEY_C = 67,
                KEY_D = 68,
                KEY_E = 69,
                KEY_F = 70,
                KEY_G = 71,
                KEY_H = 72,
                KEY_I = 73,
                KEY_J = 74,
                KEY_K = 75,
                KEY_L = 76,
                KEY_M = 77,
                KEY_N = 78,
                KEY_O = 79,
                KEY_P = 80,
                KEY_Q = 81,
                KEY_R = 82,
                KEY_S = 83,
                KEY_T = 84,
                KEY_U = 85,
                KEY_V = 86,
                KEY_W = 87,
                KEY_X = 88,
                KEY_Y = 89,
                KEY_Z = 90,
                KEY_LEFT_BRACKET = 91,
                KEY_BACKSLASH = 92,
                KEY_RIGHT_BRACKET = 93,
                KEY_GRAVE_ACCENT = 96,
                KEY_WORLD_1 = 161,
                KEY_WORLD_2 = 162;

        /** Function keys. */
        public static final int
                KEY_ESCAPE = 256,
                KEY_ENTER = 257,
                KEY_TAB = 258,
                KEY_BACKSPACE = 259,
                KEY_INSERT = 260,
                KEY_DELETE = 261,
                KEY_RIGHT = 262,
                KEY_LEFT = 263,
                KEY_DOWN = 264,
                KEY_UP = 265,
                KEY_PAGE_UP = 266,
                KEY_PAGE_DOWN = 267,
                KEY_HOME = 268,
                KEY_END = 269,
                KEY_CAPS_LOCK = 280,
                KEY_SCROLL_LOCK = 281,
                KEY_NUM_LOCK = 282,
                KEY_PRINT_SCREEN = 283,
                KEY_PAUSE = 284,
                KEY_F1 = 290,
                KEY_F2 = 291,
                KEY_F3 = 292,
                KEY_F4 = 293,
                KEY_F5 = 294,
                KEY_F6 = 295,
                KEY_F7 = 296,
                KEY_F8 = 297,
                KEY_F9 = 298,
                KEY_F10 = 299,
                KEY_F11 = 300,
                KEY_F12 = 301,
                KEY_F13 = 302,
                KEY_F14 = 303,
                KEY_F15 = 304,
                KEY_F16 = 305,
                KEY_F17 = 306,
                KEY_F18 = 307,
                KEY_F19 = 308,
                KEY_F20 = 309,
                KEY_F21 = 310,
                KEY_F22 = 311,
                KEY_F23 = 312,
                KEY_F24 = 313,
                KEY_F25 = 314,
                KEY_KP_0 = 320,
                KEY_KP_1 = 321,
                KEY_KP_2 = 322,
                KEY_KP_3 = 323,
                KEY_KP_4 = 324,
                KEY_KP_5 = 325,
                KEY_KP_6 = 326,
                KEY_KP_7 = 327,
                KEY_KP_8 = 328,
                KEY_KP_9 = 329,
                KEY_KP_DECIMAL = 330,
                KEY_KP_DIVIDE = 331,
                KEY_KP_MULTIPLY = 332,
                KEY_KP_SUBTRACT = 333,
                KEY_KP_ADD = 334,
                KEY_KP_ENTER = 335,
                KEY_KP_EQUAL = 336,
                KEY_LEFT_SHIFT = 340,
                KEY_LEFT_CONTROL = 341,
                KEY_LEFT_ALT = 342,
                KEY_LEFT_SUPER = 343,
                KEY_RIGHT_SHIFT = 344,
                KEY_RIGHT_CONTROL = 345,
                KEY_RIGHT_ALT = 346,
                KEY_RIGHT_SUPER = 347,
                KEY_MENU = 348,
                KEY_LAST = KEY_MENU;

        /** Modifier key bit masks. */
        public static final int
                MOD_SHIFT = 0x1,
                MOD_CONTROL = 0x2,
                MOD_ALT = 0x4,
                MOD_SUPER = 0x8,
                MOD_CAPS_LOCK = 0x10,
                MOD_NUM_LOCK = 0x20;
    }

    /**
     * The Joysticks class contains constants for different joystick identifiers.
     */
    public static final class Joysticks {

        public static final int
                JOYSTICK_1 = 0,
                JOYSTICK_2 = 1,
                JOYSTICK_3 = 2,
                JOYSTICK_4 = 3,
                JOYSTICK_5 = 4,
                JOYSTICK_6 = 5,
                JOYSTICK_7 = 6,
                JOYSTICK_8 = 7,
                JOYSTICK_9 = 8,
                JOYSTICK_10 = 9,
                JOYSTICK_11 = 10,
                JOYSTICK_12 = 11,
                JOYSTICK_13 = 12,
                JOYSTICK_14 = 13,
                JOYSTICK_15 = 14,
                JOYSTICK_16 = 15,
                JOYSTICK_LAST = JOYSTICK_16;
    }

    /**
     * The Gamepad class contains constants for gamepad button and axis identifiers.
     */
    public static final class Gamepad {

        public static final int
                GAMEPAD_BUTTON_A = 0,
                GAMEPAD_BUTTON_B = 1,
                GAMEPAD_BUTTON_X = 2,
                GAMEPAD_BUTTON_Y = 3,
                GAMEPAD_BUTTON_LEFT_BUMPER = 4,
                GAMEPAD_BUTTON_RIGHT_BUMPER = 5,
                GAMEPAD_BUTTON_BACK = 6,
                GAMEPAD_BUTTON_START = 7,
                GAMEPAD_BUTTON_GUIDE = 8,
                GAMEPAD_BUTTON_LEFT_THUMB = 9,
                GAMEPAD_BUTTON_RIGHT_THUMB = 10,
                GAMEPAD_BUTTON_DPAD_UP = 11,
                GAMEPAD_BUTTON_DPAD_RIGHT = 12,
                GAMEPAD_BUTTON_DPAD_DOWN = 13,
                GAMEPAD_BUTTON_DPAD_LEFT = 14,
                GAMEPAD_BUTTON_LAST = GAMEPAD_BUTTON_DPAD_LEFT,
                GAMEPAD_BUTTON_CROSS = GAMEPAD_BUTTON_A,
                GAMEPAD_BUTTON_CIRCLE = GAMEPAD_BUTTON_B,
                GAMEPAD_BUTTON_SQUARE = GAMEPAD_BUTTON_X,
                GAMEPAD_BUTTON_TRIANGLE = GAMEPAD_BUTTON_Y,
                GAMEPAD_AXIS_LEFT_X = 0,
                GAMEPAD_AXIS_LEFT_Y = 1,
                GAMEPAD_AXIS_RIGHT_X = 2,
                GAMEPAD_AXIS_RIGHT_Y = 3,
                GAMEPAD_AXIS_LEFT_TRIGGER = 4,
                GAMEPAD_AXIS_RIGHT_TRIGGER = 5,
                GAMEPAD_AXIS_LAST = GAMEPAD_AXIS_RIGHT_TRIGGER;
    }
}
