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

public abstract class LayoutComponentValidationsBuilder implements LayoutComponent {
    public LayoutPreciseValidationBuilder above(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateAboveOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.ABOVE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.ABOVE));
    }

    public LayoutPreciseValidationBuilder right(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateRightOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.RIGHT),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.RIGHT));
    }

    public LayoutPreciseValidationBuilder left(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateLeftOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.LEFT),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.LEFT));
    }

    public LayoutPreciseValidationBuilder below(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateBelowOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.BELOW),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.BELOW));
    }

    public LayoutPreciseValidationBuilder topInside(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateTopInsideOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.TOP_INSIDE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.TOP_INSIDE));
    }

    public LayoutPreciseValidationBuilder inside(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateTopInsideOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.INSIDE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.INSIDE));
    }

    public LayoutPreciseValidationBuilder bottomInside(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateBottomInsideOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.BOTTOM_INSIDE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.BOTTOM_INSIDE));
    }

    public LayoutPreciseValidationBuilder leftInside(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateLeftInsideOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.LEFT_INSIDE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.LEFT_INSIDE));
    }

    public LayoutPreciseValidationBuilder rightInside(LayoutComponent secondLayoutElement) {
        return new LayoutPreciseValidationBuilder(
                () -> calculateRightInsideOfDistance(this, secondLayoutElement),
                () -> buildNotificationValidationMessage(secondLayoutElement, LayoutOptions.RIGHT_INSIDE),
                () -> buildFailedValidationMessage(secondLayoutElement, LayoutOptions.RIGHT_INSIDE));
    }

    public LayoutPreciseValidationBuilder height() {
        return new LayoutPreciseValidationBuilder(this.getSize().getHeight());
    }

    public LayoutPreciseValidationBuilder width() {
        return new LayoutPreciseValidationBuilder(this.getSize().getHeight());
    }

    private String buildNotificationValidationMessage(LayoutComponent secondLayoutElement, LayoutOptions validationType) {
        return String.format("validate %s is %s of %s ", this.getElementName(), validationType, secondLayoutElement.getElementName());
    }

    private String buildFailedValidationMessage(LayoutComponent secondLayoutElement, LayoutOptions validationType) {
        return String.format("%s should be %s of %s ", this.getElementName(), validationType, secondLayoutElement.getElementName());
    }

    private double calculateRightOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return secondComponent.getLocation().getX() - (component.getLocation().getX() + component.getSize().getWidth());
    }

    private double calculateLeftOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return component.getLocation().getX() - (secondComponent.getLocation().getX() + secondComponent.getSize().getWidth());
    }

    private double calculateAboveOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return secondComponent.getLocation().getY() - (component.getLocation().getY() + component.getSize().getHeight());
    }

    private double calculateBelowOfDistance(LayoutComponent component, LayoutComponent secondComponent) {
        return component.getLocation().getY() - (secondComponent.getLocation().getY() + secondComponent.getSize().getHeight());
    }

    private double calculateTopInsideOfDistance(LayoutComponent innerComponent, LayoutComponent outerComponent) {
        return innerComponent.getLocation().getY() - outerComponent.getLocation().getY();
    }

    private double calculateBottomInsideOfDistance(LayoutComponent innerComponent, LayoutComponent outerComponent) {
        return (outerComponent.getLocation().getY() + outerComponent.getSize().getHeight()) - (innerComponent.getLocation().getY() + innerComponent.getSize().getHeight());
    }

    private double calculateLeftInsideOfDistance(LayoutComponent innerComponent, LayoutComponent outerComponent) {
        return innerComponent.getLocation().getX() - outerComponent.getLocation().getX();
    }

    private double calculateRightInsideOfDistance(LayoutComponent innerComponent, LayoutComponent outerComponent) {
        return (outerComponent.getLocation().getX() + outerComponent.getSize().getWidth()) - (innerComponent.getLocation().getX() + innerComponent.getSize().getWidth());
    }
}
