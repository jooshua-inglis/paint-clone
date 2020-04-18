package vector;

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

    /**
     * Tests for Polygon and Plot which have non-standard vectorpoint parameters
      */
    @Test
     void editPlotPoint(){
        VectorShape plot = new Plot();

        plot.addPoint(0.2,0.3);
        plot.editPoint(0,plot.getPoint(0));
        assertThrows(IndexOutOfBoundsException.class, () -> plot.editPoint(1,plot.getPoint(0)));
    }

    //Index 2 should not be considered out of bounds for the polygon
    @Test
    void editPolygonPoints(){
        VectorShape polygon = new Polygon();
        polygon.addPoint(0.2,0.3);
        polygon.addPoint(0.3,0.5);
        polygon.addPoint(0.4,0.3);
        polygon.editPoint(2,polygon.getPoint(0));
        assertThrows(IndexOutOfBoundsException.class, () -> polygon.editPoint(3,polygon.getPoint(0)));
    }
    /**
     * Test to show that the plot only accepts one point
     */
    @Test
    void addPlotPoints(){
        VectorShape plot = new Plot();
        plot.addPoint(0,0);
        assertThrows(IllegalStateException.class, () -> plot.addPoint(0.2,0.2), "Exceeded max VectorPoints");
        assertEquals(1,plot.getMaxPoints());
    }

    /**
     * Test to show that the polygon accepts endless points without error
     */
    @Test
    void addPolygonPoints(){
        VectorShape polygon = new Polygon();
        for( int i = 0; i<250; i++) {
            polygon.addPoint(0, 0.1);
        }
        assertEquals(250,polygon.getVectorPoints().size());
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

    /**
     * Tests to check shapes' fill and pen setters/getters
     */
    @Test
    void testGetFillRGB() {
        VectorShape rectangle = new Rectangle();
        rectangle.setFill(new VectorColor(0xCECECC));
        assertEquals("#CECECC", rectangle.getFillRGB());

        VectorShape ellipse = new Ellipse();
        ellipse.setFill(new VectorColor(0xCCCECC));
        assertEquals("#CCCECC", ellipse.getFillRGB());

        VectorShape plot = new Plot();
        plot.setFill(new VectorColor(0xEECECC));
        assertEquals("#EECECC", plot.getFillRGB());

        VectorShape line = new Line();
        line.setFill(new VectorColor(0xCEEECC));
        assertEquals("#CEEECC", line.getFillRGB());

        VectorShape polygon = new Polygon();
        polygon.setFill(new VectorColor(0xCECECE));
        assertEquals("#CECECE", polygon.getFillRGB());
    }
    @Test
    void testGetPenRGB() {
        VectorShape rectangle = new Rectangle();
        rectangle.setPen(new VectorColor(0xCECECC));
        assertEquals("#CECECC", rectangle.getPenRGB());

        VectorShape ellipse = new Ellipse();
        ellipse.setPen(new VectorColor(0xCECCCE));
        assertEquals("#CECCCE", ellipse.getPenRGB());

        VectorShape plot = new Plot();
        plot.setPen(new VectorColor(0xCEEECE));
        assertEquals("#CEEECE", plot.getPenRGB());

        VectorShape line = new Line();
        line.setPen(new VectorColor(0xCCCECE));
        assertEquals("#CCCECE", line.getPenRGB());

        VectorShape polygon = new Polygon();
        polygon.setPen(new VectorColor(0xCEEECE));
        assertEquals("#CEEECE", polygon.getPenRGB());
    }

}
