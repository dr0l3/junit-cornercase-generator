package generators;

import example.Orderline;
import generator.CombinedGenerator;

public class PlainOrderlineGenerator extends CombinedGenerator<Orderline> {
    public PlainOrderlineGenerator() {
        super(Orderline.class);
    }
}
