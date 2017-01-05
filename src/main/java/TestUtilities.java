import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author mistryd on 9/26/2016.
 */
public class TestUtilities {

    /**
     * Utility method to mock lists with given mockValues
     * TODO : Move to a proper utility class after restructure / cleanup of this
     *
     * @param mockIterable Mocked object of a collection
     * @param mockValues mockValues for the collection of type T
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <E> void mockIterable(Iterable<E> mockIterable, E... mockValues) {
        Iterator<E> mockIterator = mock(Iterator.class);
        when(mockIterable.iterator()).thenReturn(mockIterator);

        if (mockValues.length == 0) {
            when(mockIterator.hasNext()).thenReturn(false);
        } else if (mockValues.length == 1) {
            when(mockIterator.hasNext()).thenReturn(true, false);
            when(mockIterator.next()).thenReturn(mockValues[0]);
        } else {
            // build boolean array for hasNext()
            Boolean[] hasNextResponses = new Boolean[mockValues.length];
            for (int i = 0; i < hasNextResponses.length -1 ; i++) {
                hasNextResponses[i] = true;
            }
            hasNextResponses[hasNextResponses.length - 1] = false;
            when(mockIterator.hasNext()).thenReturn(true, hasNextResponses);
            E[] valuesMinusTheFirst = Arrays.copyOfRange(mockValues, 1, mockValues.length);
            when(mockIterator.next()).thenReturn(mockValues[0], valuesMinusTheFirst);
        }
    }

    /**
     * Utility method to mock lists with given values
     * TODO : Move to a proper utility class after restructure / cleanup of this
     *
     * @param mockIterable Mocked object of a collection
     * @param mockValue mockValue for the collection of type T
     * @param numberOfValues number of times same object T is to be mocked
     */
    @SuppressWarnings("unchecked")
    public static <E> void mockIterable(Iterable<E> mockIterable, E mockValue, int numberOfValues) {
        List<E> mockList = new ArrayList<>();
        for(int i=0; i<numberOfValues; i++){
            mockList.add(mockValue);
        }
        // This cast will not work for regular arrays but works for generics ( for regular arrays, we need to specify array type in toArray method as an arg
        mockIterable(mockIterable, (E[]) mockList.toArray());
    }

    /**
     * Method to mock a map. This method creates a real set of values passed in using the keys
     *
     * @param mockMap - Mock object of the map
     * @param mockValue - The mock object of values in the map
     * @param keys - List of keys in the map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void mockMap(Map<K, V> mockMap, V mockValue, K... keys) {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (K key : keys) {
            Map.Entry<K, V> entry = new AbstractMap.SimpleEntry<>(key, mockValue);
            entrySet.add(entry);
        }

        when(mockMap.entrySet()).thenReturn(entrySet);
    }

    /**
     * Method to be used by unit tests to set a private static non-primitive field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the value parameter
     * @param value     : the value to be set ( must be of the same type as the fieldName specified )
     *                  Note: This method uses the type of the 'value' to get the class where to set the value. This method does not work for primitive value types
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> void setPrivateStaticField(String fieldName, T value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    /**
     * Method to be used by unit tests to set a private static primitive field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the clazz parameter
     * @param value     : the value to be set ( primitive )
     * @param clazz     : class containing the field
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T, CT> void setPrivateStaticField(String fieldName, T value, Class<CT> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    /**
     * Method to be used by unit tests to set a private non-primitive field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the value parameter
     * @param value     : the value to be set Note: This method uses the type of the 'value' to get the class where to set the value. This method does not work for primitive value types
     * @param object    : object of the class containing the field
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> void setPrivateField(String fieldName, T value, T object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * Method to be used by unit tests to set a private primitive field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the clazz parameter
     * @param value     : the value to be set ( primitive )
     * @param object    : object of the class containing the field
     * @param clazz     : class containing the field
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T, CT> void setPrivateField(String fieldName, T value, T object, Class<CT> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * Method to be used by unit tests to set a private/public static final field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the value parameter
     * @param value     the value to be set Note: This method uses the type of the 'value' to get the class where to set the value. This method does not work for primitive value types
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> void setStaticFinal(String fieldName, T value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, value);
    }

    /**
     * Method to be used by unit tests to set a private/public static final field with a particular value (usually a mock) forcefully ( reflectively )
     *
     * @param fieldName : the exact field name in the class specified by the clazz parameter
     * @param value     the value to be set Note: This method uses the type of the 'value' to get the class where to set the value. This method does not work for primitive value types
     * @param clazz     : class containing the field
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T, CT> void setStaticFinal(String fieldName, T value, Class<CT> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, value);
    }
}
