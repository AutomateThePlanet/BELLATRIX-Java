package O18_test_workflow_hooks;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.infrastructure.junit.WebTest;

public class TestWorkflowHooksTests extends WebTest {
    private static Select sortDropDown;
    private static Anchor protonRocketAnchor;

//    @Override
//    public void beforeClass() {
//        // Executes a logic once before all tests in the test class.
//    }
//
//    @Override
//    public void beforeMethod() {
//        sortDropDown =
//                app().create().byXpath(Select.class, "//*[@id='main']/div[1]/form/select");
//        protonRocketAnchor =
//                app().create().byXpath(Anchor.class, "//*[@id='main']/div[2]/ul/li[1]/a[1]");
//
//        app().navigate().to("http://demos.bellatrix.solutions/");
//
//        sortDropDown.selectByText("Sort by price: low to high");
//    }
//
//    @Override
//    public void afterMethod() {
//        // Executes a logic after each test in the test class.
//    }
//
//    @Override
//    public void afterClass() {
//        // Executes a logic once after all tests in the test class.
//    }

    @Test
    public void sortDropDownIsAboveOfProtonRocketAnchor() {
        sortDropDown.above(protonRocketAnchor).validate();
    }

    @Test
    public void sortDropDownIsAboveOfProtonRocketAnchor_41px() {
        sortDropDown.above(protonRocketAnchor).equal(41).validate();
    }

    @Test
    public void sortDropDownIsAboveOfProtonRocketAnchor_GreaterThan40px() {
        sortDropDown.above(protonRocketAnchor).greaterThan(40).validate();
    }

    @Test
    public void sortDropDownIsAboveOfProtonRocketAnchor_GreaterThanOrEqual41px() {
        sortDropDown.above(protonRocketAnchor).greaterThanOrEqual(41).validate();
    }

    @Test
    public void sortDropDownIsNearTopOfProtonRocketAnchor_GreaterThan40px() {
        sortDropDown.topInside(protonRocketAnchor).greaterThan(40).validate();
    }
}