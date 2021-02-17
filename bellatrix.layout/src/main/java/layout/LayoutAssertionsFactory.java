/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package layout;

import org.testng.Assert;
import solutions.bellatrix.plugins.EventListener;

public class LayoutAssertionsFactory {
    // SINGLE EVENT? Use Builder
    public final static EventListener<LayoutTwoComponentsNoExpectedActionEventArgs> ASSERTED_ABOVE_OF_NO_EXPECTED_VALUE = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionEventArgs> ASSERTED_ABOVE_OF = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionTwoValuesEventArgs> ASSERTED_ABOVE_OF_BETWEEN = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionEventArgs> ASSERTED_ABOVE_OF_GREATER_THAN = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionEventArgs> ASSERTED_ABOVE_OF_GREATER_OR_EQUAL = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionEventArgs> ASSERTED_ABOVE_OF_LESS_THAN = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionEventArgs> ASSERTED_ABOVE_OF_LESS_OR_EQUAL = new EventListener<>();
    public final static EventListener<LayoutTwoComponentsActionTwoValuesEventArgs> ASSERTED_ABOVE_OF_APPROXIMATE = new EventListener<>();

    private LayoutComponent component;

    public LayoutAssertionsFactory(LayoutComponent component) {
        this.component = component;
    }

    public void assertAboveOf(LayoutComponent secondComponent) {
        double actualDistance = calculateAboveOfDistance(component, secondComponent);
        Assert.assertTrue(actualDistance >= -1, String.format("%s should be above of %s but was %d px.", component.getElementName(), secondComponent.getElementName(), actualDistance));
        ASSERTED_ABOVE_OF_NO_EXPECTED_VALUE.broadcast(new LayoutTwoComponentsNoExpectedActionEventArgs(component, secondComponent));
    }

    public void assertAboveOf(LayoutComponent secondComponent, double expected) {
        double actualDistance = calculateAboveOfDistance(component, secondComponent);
        Assert.assertEquals(expected, actualDistance, String.format("%s should be %d px above of %s but was %d px.", component.getElementName(), expected, secondComponent.getElementName(), actualDistance));
        ASSERTED_ABOVE_OF.broadcast(new LayoutTwoComponentsActionEventArgs(component, secondComponent, Double.toString(expected)));
    }

//    public void assertAboveOfBetween(LayoutComponent component, LayoutComponent secondComponent, double from, double to) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualDistance >= from && actualDistance <= to, $
//        "{component.componentName} should be between {from}-{to} px above of {secondComponent.componentName}, but {actualDistance}.")
//        ;
//        AssertedAboveOfBetweenEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionTwoValuesEventArgs(component, secondComponent, from, to));
//    }
//
//    public void assertAboveOfGreaterThan(LayoutComponent component, LayoutComponent secondComponent, double expected) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualDistance > expected, $
//        "{component.componentName} should be > {expected} px above of {secondComponent.componentName} but was {actualDistance} px.")
//        ;
//        AssertedAboveOfGreaterThanEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionEventArgs(component, secondComponent, expected));
//    }
//
//    public void assertAboveOfGreaterThanOrEqual(LayoutComponent component, LayoutComponent secondComponent, double expected) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualDistance >= expected, $
//        "{component.componentName} should be >= {expected} px above of {secondComponent.componentName} but was {actualDistance} px.")
//        ;
//        AssertedAboveOfGreaterOrEqualThanEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionEventArgs(component, secondComponent, expected));
//    }
//
//    public void assertAboveOfLessThan(LayoutComponent component, LayoutComponent secondComponent, double expected) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualDistance < expected, $
//        "{component.componentName} should be < {expected} px above of {secondComponent.componentName} but was {actualDistance} px.")
//        ;
//        AssertedAboveOfLessThanEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionEventArgs(component, secondComponent, expected));
//    }
//
//    public void assertAboveOfLessThanOrEqual(LayoutComponent component, LayoutComponent secondComponent, double expected) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualDistance <= expected, $
//        "{component.componentName} should be <= {expected} px above of {secondComponent.componentName} but was {actualDistance} px.")
//        ;
//        AssertedAboveOfLessOrEqualThanEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionEventArgs(component, secondComponent, expected));
//    }
//
//    public void assertAboveOfApproximate(LayoutComponent component, LayoutComponent secondComponent, double expected, double percent) {
//        var actualDistance = calculateAboveOfDistance(component, secondComponent);
//        var actualPercentDifference = CalculatePercentDifference(expected, actualDistance);
//        BA.Assert.IsTrue<LayoutAssertFailedException> (actualPercentDifference <= percent, $
//        "{component.componentName} should be <= {percent}% of {expected} px above of {secondComponent.componentName} but was {actualDistance} px.")
//        ;
//        AssertedAboveOfApproximateEvent ?.
//        Invoke(component, new LayoutTwocomponentsActionTwoValuesEventArgs(component, secondComponent, expected, percent));
//    }


    private double CalculatePercentDifference(double num1, double num2) {
        var percentDifference = (num1 - num2) / ((num1 + num2) / 2) * 100;
        var actualPercentDifference = Math.abs(percentDifference);

        return actualPercentDifference;
    }

    private double CalculateRightOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return secondComponent.getLocation().getX() - (component.getLocation().getX() + component.getSize().getWidth());
    }

    private double CalculateLeftOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return component.getLocation().getX() - (secondComponent.getLocation().getX() + secondComponent.getSize().getWidth());
    }

    private double calculateAboveOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return secondComponent.getLocation().getY() - (component.getLocation().getY() + component.getSize().getHeight());
    }

    private double CalculateBelowOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return component.getLocation().getY() - (secondComponent.getLocation().getY() + secondComponent.getSize().getHeight());
    }

    private double CalculateTopInsideOfDistance(LayoutComponent innercomponent, LayoutComponent outercomponent) {
        return innercomponent.getLocation().getY() - outercomponent.getLocation().getY();
    }

    private double CalculateBottomInsideOfDistance(LayoutComponent innercomponent, LayoutComponent outercomponent) {
        return (outercomponent.getLocation().getY() + outercomponent.getSize().getHeight()) - (innercomponent.getLocation().getY() + innercomponent.getSize().getHeight());
    }

    private double CalculateLeftInsideOfDistance(LayoutComponent innercomponent, LayoutComponent outercomponent) {
        return innercomponent.getLocation().getX() - outercomponent.getLocation().getX();
    }

    private double CalculateRightInsideOfDistance(LayoutComponent innercomponent, LayoutComponent outercomponent) {
        return (outercomponent.getLocation().getX() + outercomponent.getSize().getWidth()) - (innercomponent.getLocation().getX() + innercomponent.getSize().getWidth());
    }
}
