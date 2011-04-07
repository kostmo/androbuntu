precision mediump float;
void main() {

	int diamond_radius = 32;

	int window_height = 480;
	int period = 2*diamond_radius;

	int w = wave(gl_FragCoord.x, diamond_radius, period);
	int manhattan_dist = wave(gl_FragCoord.x, diamond_radius, period) + wave(gl_FragCoord.y, diamond_radius, period);
	int recentered_manhattan_dist = abs(manhattan_dist - diamond_radius);

	float brightness = recentered_manhattan_dist / float(diamond_radius);

	gl_FragColor = vec4(0.0, 0.0, brightness, 1.0);
}

