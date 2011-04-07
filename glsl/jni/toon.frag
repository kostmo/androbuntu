// simple toon fragment shader
// www.lighthouse3d.com

varying vec3 normal, lightDir;
uniform int clock_ticker, diamond_radius, clock_period;

int wave(int x, int diamond_radius, int period) {	// Generates a triangle wave
	int sawtooth = x % period;
	int centered_sawtooth = sawtooth - diamond_radius;
	int triangle = abs(centered_sawtooth);
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

    switch(hueIndex)
    {
        case 0:
            result = vec3(v,t,p);
        break;
        case 1:
            result = vec3(q,v,p);
        break;
        case 2:
            result = vec3(p,v,t);
        break;
        case 3:
            result = vec3(p,q,v);
        break;
        case 4:
            result = vec3(t,p,v);
        break;
        case 5:
            result = vec3(v,p,q);
        break;
    }
    return result;
}

void main()
{
	/*
	float intensity;
	vec3 n;
	vec4 color;

	n = normalize(normal);
	intensity = max(dot(lightDir,n),0.0); 

	if (intensity > 0.98)
		color = vec4(0.8,0.8,0.8,1.0);
	else if (intensity > 0.5)
		color = vec4(0.4,0.4,0.8,1.0);	
	else if (intensity > 0.25)
		color = vec4(0.2,0.2,0.4,1.0);
	else
		color = vec4(0.1,0.1,0.1,1.0);		
	*/

	int window_height = 640;
	ivec2 screenpos = ivec2(gl_FragCoord.xy);

	int period = 2*diamond_radius;

	int w = wave(screenpos[0], diamond_radius, period);
	int manhattan_dist = wave(screenpos[0], diamond_radius, period) + wave(screenpos[1], diamond_radius, period);
	int recentered_manhattan_dist = abs(manhattan_dist - diamond_radius);

//	int quadrature = diamond_radius / 2;
//	recentered_manhattan_dist = quadrature - abs(recentered_manhattan_dist - quadrature);
//	float brightness = recentered_manhattan_dist / float(quadrature);

	float brightness = recentered_manhattan_dist / float(diamond_radius);

	int cycle_granularity = 1000;

	int cycle_phase = screenpos[1]*cycle_granularity/window_height + clock_ticker*cycle_granularity/clock_period;

	float vertical_fraction = (cycle_phase % cycle_granularity)/float(cycle_granularity);
	gl_FragColor = vec4(HSVtoRGB(vec3(vertical_fraction, 1.0, brightness)), 1.0);
}
