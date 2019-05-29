package tests;

import org.junit.jupiter.api.Test;
import vector.shape.Polygon;
import vector.shape.Rectangle;
import vector.shape.Plot;
import vector.shape.Line;
import vector.shape.Ellipse;
import vector.shape.VectorShape;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShapeTests {
    void testPoints(double expectedX, double expectedY, VectorShape subject){
        assertEquals(expectedX, subject.getPoint(0).getX());
        assertEquals(expectedY, subject.getPoint(0).getY());
    }

    @Test
    void exceptionTest(){
        VectorShape subject = new Rectangle();
        assertThrows(IllegalArgumentException.class, () -> subject.addPoint(1.1,1.1), "Invalid Shape size");
        subject.addPoint(0,0);
        subject.addPoint(0.2,0.3);
        assertThrows(IllegalStateException.class, () -> subject.addPoint(0.2,0.2), "Exceeded max VectorPoints");
    }

    @Test
    void equalsTestRectangle() {
        Rectangle subject1 = new Rectangle();
        subject1.addPoint(0.2,0.6);
        subject1.addPoint(0.8,0.7);
        Rectangle subject2 = new Rectangle();
        subject2.addPoint(0.2,0.6);
        subject2.addPoint(0.8,0.7);
        assertEquals(subject1, subject2);
    }
    @Test
    void equalsTestPolygon() {
        Polygon subject1 = new Polygon();
        subject1.addPoint(0.2,0.6);
        subject1.addPoint(0.8,0.7);
        subject1.addPoint(0.3,0.6);
        Polygon subject2 = new Polygon();
        subject1.addPoint(0.2,0.6);
        subject1.addPoint(0.8,0.7);
        subject1.addPoint(0.3,0.6);
        assertEquals(subject1, subject2);
    }
    @Test
    void equalsTestEllipse() {
        Ellipse subject1 = new Ellipse();
        subject1.addPoint(0.2,0.6);
        subject1.addPoint(0.8,0.7);
        Ellipse subject2 = new Ellipse();
        subject2.addPoint(0.2,0.6);
        subject2.addPoint(0.8,0.7);
        assertEquals(subject1, subject2);
    }
    @Test
    void equalsTestLine() {
        Line subject1 = new Line();
        subject1.addPoint(0.2,0.6);
        subject1.addPoint(0.8,0.7);
        Line subject2 = new Line();
        subject2.addPoint(0.2,0.6);
        subject2.addPoint(0.8,0.7);
        assertEquals(subject1, subject2);
    }
    @Test
    void equalsTestPlot() {
        Plot subject1 = new Plot();
        subject1.addPoint(0.2,0.6);
        Plot subject2 = new Plot();
        subject2.addPoint(0.2,0.6);
        assertEquals(subject1, subject2);
    }

    @Test
    void addPoint() {
        VectorShape subject = new Rectangle();
        try {
            subject.addPoint(0.2,0.3);
        } catch (IllegalArgumentException error) { fail("Exception when adding point");}

        testPoints(0.2, 0.3, subject);
    }

    @Test
    void editPoints() {
        VectorShape subject = new Rectangle();
        try {
            subject.addPoint(0.2, 0.3);
        } catch (IllegalArgumentException error) {
            fail("Exception when adding point");
        }

        try { subject.editPoint(0, new VectorPoint(0.6, 0.3)); }
        catch (IllegalArgumentException error) { }

        testPoints(0.6, 0.3, subject);
    }

    @Test
    void asList() {
        VectorShape subject = new Rectangle();
        try {
            subject.addPoint(0.2,0.3);
            subject.addPoint(0.5, 0.2);
        } catch (IllegalArgumentException error) { fail("Exception when adding point");}

        assertEquals(Arrays.asList(0.2, 0.3, 0.5, 0.2), subject.asList());
    }

    @Test
    void testGetVec() {
        VectorShape subject = new Rectangle();
        try {
            subject.addPoint(0.2,0.3);
            subject.addPoint(0.5, 0.2);
        } catch (IllegalArgumentException error) { fail("Exception when adding point");}

        try {
            assertEquals("RECTANGLE 0.20 0.30 0.50 0.20", subject.getVec(false, false));
        } catch (IllegalArgumentException error) { fail(error.getMessage()); }
    }

    @Test
    void testGetPenRGB() {
        VectorShape subject = new Rectangle();
        subject.setFill(new VectorColor(0xFF));
        assertEquals("#0000FF", subject.getFillRGB());
    }
}
