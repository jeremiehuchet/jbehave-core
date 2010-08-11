package org.jbehave.core.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import org.jbehave.core.steps.ParameterConverters.MethodReturningConverter;
import org.jbehave.core.steps.ParameterConverters.NumberConverter;
import org.jbehave.core.steps.ParameterConverters.NumberListConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConvertionFailed;
import org.jbehave.core.steps.ParameterConverters.StringListConverter;
import org.junit.Test;

public class ParameterConvertersBehaviour {

    @Test
    public void shouldConvertValuesToNumbers() {
        ParameterConverter converter = new NumberConverter();
        assertThat((Integer) converter.convertValue("3", Integer.class), equalTo(3));
        assertThat((Integer) converter.convertValue("3", int.class), equalTo(3));
        assertThat((Float) converter.convertValue("3.0", Float.class), equalTo(3.0f));
        assertThat((Float) converter.convertValue("3.0", float.class), equalTo(3.0f));
        assertThat((Long) converter.convertValue("3", Long.class), equalTo(3L));
        assertThat((Long) converter.convertValue("3", long.class), equalTo(3L));
        assertThat((Double) converter.convertValue("3.0", Double.class), equalTo(3.0d));
        assertThat((Double) converter.convertValue("3.0", double.class), equalTo(3.0d));
        assertThat((BigDecimal) converter.convertValue("3.0", BigDecimal.class), equalTo(new BigDecimal("3.0")));
        assertThat((BigInteger) converter.convertValue("3", BigInteger.class), equalTo(new BigInteger("3")));
        assertThat((String) converter.convertValue("3.0", String.class), equalTo("3.0"));
    }

    @Test
    public void shouldConvertNaNAndInfinityValuesToNumbers() {
        ParameterConverter converter = new NumberConverter();
        assertThat((Float) converter.convertValue("Infinity", Float.class), equalTo(Float.POSITIVE_INFINITY));
        assertThat((Float) converter.convertValue("-Infinity", Float.class), equalTo(Float.NEGATIVE_INFINITY));
        assertThat((Float) converter.convertValue("NaN", Float.class), equalTo(Float.NaN));
        assertThat((Double) converter.convertValue("NaN", Double.class), equalTo(Double.NaN));
        assertThat((Double) converter.convertValue("Infinity", Double.class), equalTo(Double.POSITIVE_INFINITY));
        assertThat((Double) converter.convertValue("-Infinity", Double.class), equalTo(Double.NEGATIVE_INFINITY));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConvertCommaSeparatedValuesToListOfNumbers() throws ParseException, IntrospectionException {
        ParameterConverter converter = new NumberListConverter();
        Type listOfNumbers = SomeSteps.methodFor("aMethodWithListOfNumbers").getGenericParameterTypes()[0];
        Type setOfNumbers = SomeSteps.methodFor("aMethodWithSetOfNumbers").getGenericParameterTypes()[0];
        assertThat(converter.accept(listOfNumbers), is(true));
        assertThat(converter.accept(setOfNumbers), is(false));
        List<Number> list = (List<Number>) converter.convertValue("3, 0.5, 6.1f, 8.00", listOfNumbers);
        NumberFormat numberFormat = NumberFormat.getInstance();
        assertThat(list.get(0), equalTo(numberFormat.parse("3")));
        assertThat(list.get(1), equalTo(numberFormat.parse("0.5")));
        assertThat(list.get(2), equalTo(numberFormat.parse("6.1f")));
        assertThat(list.get(3), equalTo(numberFormat.parse("8.00")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConvertCommaSeparatedValuesToListOfNumbersWithCustomFormat() throws ParseException,
            IntrospectionException {
        NumberFormat numberFormat = new DecimalFormat("#,####");
        ParameterConverter converter = new NumberListConverter(numberFormat, " ");
        Type type = SomeSteps.methodFor("aMethodWithListOfNumbers").getGenericParameterTypes()[0];
        List<Number> list = (List<Number>) converter.convertValue("3,000 0.5 6.1f 8.00", type);
        assertThat(list.get(0), equalTo(numberFormat.parse("3,000")));
        assertThat(list.get(1), equalTo(numberFormat.parse("0.5")));
        assertThat(list.get(2), equalTo(numberFormat.parse("6.1f")));
        assertThat(list.get(3), equalTo(numberFormat.parse("8.00")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConvertCommaSeparatedValuesOfSpecificNumberTypes() throws ParseException, IntrospectionException {
        ParameterConverter converter = new NumberListConverter();
        Type doublesType = SomeSteps.methodFor("aMethodWithListOfDoubles").getGenericParameterTypes()[0];
        List<Double> doubles = (List<Double>) converter.convertValue("3, 0.5, 0.0, 8.00, NaN, Infinity", doublesType);
        assertThat(doubles.get(0), equalTo(3.0d));
        assertThat(doubles.get(1), equalTo(0.5d));
        assertThat(doubles.get(2), equalTo(0.0d));
        assertThat(doubles.get(3), equalTo(8.00d));
        assertThat(doubles.get(4), equalTo(Double.NaN));
        assertThat(doubles.get(5), equalTo(Double.POSITIVE_INFINITY));

        Type floatsType = SomeSteps.methodFor("aMethodWithListOfFloats").getGenericParameterTypes()[0];
        List<Float> floats = (List<Float>) converter.convertValue("3, 0.5, 0.0, 8.00, NaN, -Infinity", floatsType);
        assertThat(floats.get(0), equalTo(3.0f));
        assertThat(floats.get(1), equalTo(0.5f));
        assertThat(floats.get(2), equalTo(0.0f));
        assertThat(floats.get(3), equalTo(8.00f));
        assertThat(floats.get(4), equalTo(Float.NaN));
        assertThat(floats.get(5), equalTo(Float.NEGATIVE_INFINITY));

        Type longsType = SomeSteps.methodFor("aMethodWithListOfLongs").getGenericParameterTypes()[0];
        List<Long> longs = (List<Long>) converter.convertValue("3, 0, 8", longsType);
        assertThat(longs.get(0), equalTo(3L));
        assertThat(longs.get(1), equalTo(0L));
        assertThat(longs.get(2), equalTo(8L));

        Type intsType = SomeSteps.methodFor("aMethodWithListOfIntegers").getGenericParameterTypes()[0];
        List<Integer> ints = (List<Integer>) converter.convertValue("3, 0, 8", intsType);
        assertThat(ints.get(0), equalTo(3));
        assertThat(ints.get(1), equalTo(0));
        assertThat(ints.get(2), equalTo(8));
    }

    @Test(expected = ParameterConvertionFailed.class)
    public void shouldFailToConvertCommaSeparatedValuesOfInvalidNumbers() throws ParseException, IntrospectionException {
        ParameterConverter converter = new NumberListConverter();
        Type type = SomeSteps.methodFor("aMethodWithListOfNumbers").getGenericParameterTypes()[0];
        converter.convertValue("3x, x.5", type);
    }

    @Test
    public void shouldConvertCommaSeparatedValuesToListOfStrings() throws IntrospectionException {
        ParameterConverter converter = new StringListConverter();
        Type listOfStrings = SomeSteps.methodFor("aMethodWithListOfStrings").getGenericParameterTypes()[0];
        Type listOfNumbers = SomeSteps.methodFor("aMethodWithListOfNumbers").getGenericParameterTypes()[0];
        Type setOfNumbers = SomeSteps.methodFor("aMethodWithSetOfNumbers").getGenericParameterTypes()[0];
        assertThat(converter.accept(listOfStrings), is(true));
        assertThat(converter.accept(listOfNumbers), is(false));
        assertThat(converter.accept(setOfNumbers), is(false));
        ensureValueIsConvertedToList(converter, listOfStrings, "a, string ", Arrays.asList("a", "string"));
        ensureValueIsConvertedToList(converter, listOfStrings, " ", Arrays.asList(new String[] {}));
    }

    @SuppressWarnings("unchecked")
    private void ensureValueIsConvertedToList(ParameterConverter converter, Type type, String value,
            List<String> expected) {
        List<String> list = (List<String>) converter.convertValue(value, type);
        assertThat(list.size(), equalTo(expected.size()));
    }

    @Test
    public void shouldConvertDateWithDefaultFormat() throws ParseException, IntrospectionException {
        ParameterConverter converter = new DateConverter();
        assertThat(converter.accept(Date.class), equalTo(true));
        assertThat(converter.accept(WrongType.class), is(false));
        assertThat(converter.accept(mock(Type.class)), is(false));
        Type type = SomeSteps.methodFor("aMethodWithDate").getGenericParameterTypes()[0];
        String date = "01/01/2010";
        assertThat((Date) converter.convertValue(date, type), equalTo(DateConverter.DEFAULT_FORMAT.parse(date)));
    }

    @Test
    public void shouldConvertDateWithCustomFormat() throws ParseException, IntrospectionException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        ParameterConverter converter = new DateConverter(customFormat);
        assertThat(converter.accept(Date.class), equalTo(true));
        assertThat(converter.accept(WrongType.class), is(false));
        assertThat(converter.accept(mock(Type.class)), is(false));
        Type type = SomeSteps.methodFor("aMethodWithDate").getGenericParameterTypes()[0];
        String date = "2010-01-01";
        assertThat((Date) converter.convertValue(date, type), equalTo(customFormat.parse(date)));
    }

    @Test(expected = ParameterConvertionFailed.class)
    public void shouldFailToConvertDateWithInvalidFormat() throws ParseException, IntrospectionException {
        Type type = SomeSteps.methodFor("aMethodWithDate").getGenericParameterTypes()[0];
        ParameterConverter converter = new DateConverter();
        String date = "dd+MM+yyyy";
        converter.convertValue(date, type);
    }

    @Test
    public void shouldConvertMultilineTableParameter() throws ParseException, IntrospectionException {
        ParameterConverter converter = new ExamplesTableConverter();
        assertThat(converter.accept(ExamplesTable.class), is(true));
        assertThat(converter.accept(WrongType.class), is(false));
        assertThat(converter.accept(mock(Type.class)), is(false));
        Type type = SomeSteps.methodFor("aMethodWithExamplesTable").getGenericParameterTypes()[0];
        String value = "|col1|col2|\n|row11|row12|\n|row21|row22|\n";
        ExamplesTable table = (ExamplesTable) converter.convertValue(value, type);
        assertThat(table.getRowCount(), equalTo(2));
        Map<String, String> row1 = table.getRow(0);
        assertThat(row1.get("col1"), equalTo("row11"));
        assertThat(row1.get("col2"), equalTo("row12"));
        Map<String, String> row2 = table.getRow(1);
        assertThat(row2.get("col1"), equalTo("row21"));
        assertThat(row2.get("col2"), equalTo("row22"));
    }

    @Test
    public void shouldConvertParameterFromMethodReturningValue() throws ParseException, IntrospectionException {
        Method method = SomeSteps.methodFor("aMethodReturningExamplesTable");
        ParameterConverter converter = new MethodReturningConverter(method, new SomeSteps());
        assertThat(converter.accept(method.getReturnType()), is(true));
        assertThat(converter.accept(WrongType.class), is(false));
        assertThat(converter.accept(mock(Type.class)), is(false));
        String value = "|col1|col2|\n|row11|row12|\n|row21|row22|\n";
        ExamplesTable table = (ExamplesTable) converter.convertValue(value, ExamplesTable.class);
        assertThat(table.getRowCount(), equalTo(2));
        Map<String, String> row1 = table.getRow(0);
        assertThat(row1.get("col1"), equalTo("row11"));
        assertThat(row1.get("col2"), equalTo("row12"));
        Map<String, String> row2 = table.getRow(1);
        assertThat(row2.get("col1"), equalTo("row21"));
        assertThat(row2.get("col2"), equalTo("row22"));
    }

    @Test(expected = ParameterConvertionFailed.class)
    public void shouldFailToConvertParameterFromFailingMethodReturningValue() throws ParseException,
            IntrospectionException {
        Method method = SomeSteps.methodFor("aFailingMethodReturningExamplesTable");
        ParameterConverter converter = new MethodReturningConverter(method, new SomeSteps());
        String value = "|col1|col2|\n|row11|row12|\n|row21|row22|\n";
        converter.convertValue(value, ExamplesTable.class);
    }

    static class WrongType {

    }
}
