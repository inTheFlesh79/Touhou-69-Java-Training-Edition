package BulletHellPatterns;

import Enemies.EnemyBullet;

public class CirclePattern extends BulletHellPattern {
    private int currentBullet = 0;

    public CirclePattern() {
        cantBullet = 12;  // Cantidad de balas por círculo
        angle = 0;        // Ángulo inicial
        speed = 200f;     // Velocidad baja para un movimiento lento
        maxShootingTime = 5.0f;
        bulletGenInterval = 0.5f;
    }

    @Override
    public EnemyBullet generateBulletInPattern(float x, float y) {
        // Calculamos la dirección de la bala usando el ángulo y el índice actual
        float direction = angle + (float) (2 * Math.PI * currentBullet / cantBullet);
        
        // Calculamos las componentes de la velocidad de la bala
        float bulletVelocityX = (float) Math.cos(direction) * speed;
        float bulletVelocityY = (float) Math.sin(direction) * speed;

        // Creamos la bala con la posición y velocidad calculadas
        EnemyBullet bullet = new EnemyBullet(x, y, bulletVelocityX, bulletVelocityY);
        
        // Aumentamos el ángulo para que el siguiente disparo sea en una dirección diferente
        angle += 0.02f;  // Cambiar el ángulo lentamente para mantener la forma circular
        
        // Avanzamos al siguiente índice de bala
        currentBullet++;
        
        // Reiniciamos el contador si todas las balas han sido disparadas
        if (currentBullet >= cantBullet) {
            currentBullet = 0;
        }

        return bullet;
    }
}