package com.app.util.input;

import com.app.api.Input;

/**
 * Contains classes related to handling collections and data structures.
 */
import java.util.ArrayList;
import java.util.Map;

public class Update {
    /**
     * Updates the state of keys based on the input event.
     *
     * @param keys     List of keys to update.
     * @param keyCode  The key code associated with the event.
     * @param newState The new state to set for the keys.
     */
    public static void keyState(Map<Integer, Input.KeyboardEvent> keys, int keyCode, Input.Type newState) {
        keys.keySet().stream()
                .filter(key -> key == keyCode).filter(key -> !keys.get(key).isBinded())
                .forEach(key -> state(keys.get(key), newState));
    }

    /**
     * Updates the state of bindings based on the input event.
     *
     * @param bindings List of bindings to update.
     * @param keyCode  The key code associated with the event.
     * @param newState The new state to set for the bindings.
     */
    public static void bindingState(ArrayList<Input.KeyBindingEvent> bindings, int keyCode, Input.Type newState) {
        bindings.stream()
                .filter(binding -> binding.getKeys().get(0).getKey() == keyCode || binding.getKeys().get(1).getKey() == keyCode)
                .forEach(binding -> {
                    Input.KeyboardEvent key0 = binding.getKeys().get(0);
                    Input.KeyboardEvent key1 = binding.getKeys().get(1);

                    if (key0.getKey() == keyCode) {
                        state(key0, newState);
                    } else if (key1.getKey() == keyCode) {
                        state(key1, newState);
                    }

                    switch (newState) {
                        case Press:
                            if (binding.isPressing()) {
                                state(binding, newState);
                            }
                            break;
                        case Release:
                            if (binding.isReleased()) {
                                state(binding, newState);
                            }
                            break;
                        case Hold:
                            if (binding.isHolding()) {
                                state(binding, newState);
                            }
                            break;
                        default:
                            return;
                    }
                });
    }

    /**
     * Updates the state of an input entity (key/binding) based on the new state.
     *
     * @param inputEntity The input entity to update.
     * @param newState    The new state to set.
     */
    private static void state(Input.KeyboardEvent inputEntity, Input.Type newState) {
        inputEntity.setState(newState);
    }

    /**
     * Updates the state of an input entity (key/binding) based on the new state.
     *
     * @param inputEntity The input entity to update.
     * @param newState    The new state to set.
     */
    private static void state(Input.KeyBindingEvent inputEntity, Input.Type newState) {
        inputEntity.setState(newState);
    }
}
