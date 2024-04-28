precision highp float;

attribute vec4 a_Position1;
attribute vec4 a_Position2;
attribute vec4 a_Position3;

attribute vec4 a_Color;

varying vec4 v_Color;

uniform mat4 u_Matrix;
uniform float u_Time;

void main()
{
    float A = 0.06 * 2.0;
    float T = 8.0;

    float Y1 =
    + A * sin(T * (a_Position1.x + u_Time * 0.5))
    + A * cos(T * (a_Position1.z + u_Time * 0.5));

    float Y2 =
    + A * sin(T * (a_Position2.x + u_Time * 0.5))
    + A * cos(T * (a_Position2.z + u_Time * 0.5));

    float Y3 =
    + A * sin(T * (a_Position3.x + u_Time * 0.5))
    + A * cos(T * (a_Position3.z + u_Time * 0.5));

    vec4 newPos1 = vec4(a_Position1.x,a_Position1.y + Y1,a_Position1.z,a_Position1.w);
    vec4 newPos2 = vec4(a_Position2.x,a_Position2.y + Y2,a_Position2.z,a_Position2.w);
    vec4 newPos3 = vec4(a_Position3.x,a_Position3.y + Y3,a_Position3.z,a_Position3.w);

    gl_Position = u_Matrix * newPos1;

    /////////////////////////////////////////

    float x1 = newPos1.x;
    float y1 = -newPos1.z;
    float z1 = newPos1.y;

    float x2 = newPos2.x;
    float y2 = -newPos2.z;
    float z2 = newPos2.y;

    float x3 = newPos3.x;
    float y3 = -newPos3.z;
    float z3 = newPos3.y;

    ///
    float bx = x2 - x1;
    float by = y2 - y1;
    float bz = z2 - z1;

    float ax = x3 - x1;
    float ay = y3 - y1;
    float az = z3 - z1;

    float vx1 = by*az - ay*bz;
    float vy1 = ax*bz - bx*az;
    float vz1 = bx*ay - ax*by;
    ///

    float vx2 = 3.0;
    float vy2 = 0.0;
    float vz2 = 10.0;

    float cosA = (vx1*vx2 + vy1*vy2 + vz1*vz2) / (sqrt(vx1*vx1 + vy1*vy1 + vz1*vz1) * sqrt(vx2*vx2 + vy2*vy2 + vz2*vz2));
    float angle = acos(cosA);


    float light = (angle/(3.141592 / 2.0)) * 1.2;

    vec4 newColor = vec4(a_Color.r - light ,a_Color.g - light*0.8 ,a_Color.b ,a_Color.a);


    v_Color = newColor;
}
