precision mediump float;

int wave(int x, int diamond_radius, int period) {	// Generates a triangle wave
	int sawtooth = x % period;
	int centered_sawtooth = sawtooth - diamond_radius;
	int triangle = int(abs(float(centered_sawtooth)));
	return triangle;
}


vec3 HSVtoRGB(vec3 color)
{
    float f,p,q,t,v, hueRound;
    int hueIndex;
    float hue, saturation, value;
    vec3 result;

    /* just for clarity */
    hue = color.r;
    saturation = color.g;
    value = color.b;
    v = value;
    hueRound = floor(hue * 6.0);
    hueIndex = int(hueRound) % 6;
    f = (hue * 6.0) - hueRound;
    p = value * (1.0 - saturation);
    q = value * (1.0 - f*saturation);
    t = value * (1.0 - (1.0 - f)*saturation);

    if (hueIndex == 0 || hueIndex == 6)
        result = vec3(v,t,p);
    else if (hueIndex == 1)
        result = vec3(q,v,p);
    else if (hueIndex == 2)
        result = vec3(p,v,t);
    else if (hueIndex == 3)
        result = vec3(p,q,v);
    else if (hueIndex == 4)
        result = vec3(t,p,v);
    else if (hueIndex == 5)
        result = vec3(v,p,q);

    return result;
}

void main()
{
	int clock_ticker = 0;
	int diamond_radius = 32;
	int clock_period = 250;

	int window_height = 854;

	int period = 2*diamond_radius;

	int manhattan_dist = wave(int(gl_FragCoord.x), diamond_radius, period) + wave(int(gl_FragCoord.y), diamond_radius, period);
	int recentered_manhattan_dist = int(abs(float(manhattan_dist - diamond_radius)));

	float brightness = float(recentered_manhattan_dist) / float(diamond_radius);

	int cycle_granularity = 1000;

	int cycle_phase = int(gl_FragCoord.y)*cycle_granularity/window_height + clock_ticker*cycle_granularity/clock_period;

	float vertical_fraction = float(cycle_phase % cycle_granularity)/float(cycle_granularity);
	gl_FragColor = vec4(HSVtoRGB(vec3(vertical_fraction, 1.0, brightness)), 1.0);
}
