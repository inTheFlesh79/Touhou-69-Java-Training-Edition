package BulletHellPatterns;

import Enemies.EnemyBullet;

public class ForkPattern extends BulletHellPattern {
    private float spreadAngle;  // Ángulo de separación entre disparos

    public ForkPattern() {
        this.cantBullet = 3;  // Tres disparos para el tenedor
        this.speed = 300f;  // Velocidad de las balas
        this.spreadAngle = (float) Math.PI /6;  // Ángulo de separación entre las balas
        this.angle = (3.0f * (float)Math.PI) / 2; // Usando 3.0f para asegurar que se trate como float
        maxShootingTime = 4.0f;
        bulletGenInterval = 0.25f;
        currentBullet = 0; 
    }

    @Override
    public void generateBulletInPattern(float x, float y, EnemyBullet bullet) {
        // Calcular la dirección de los disparos
        float direction;
        switch (currentBullet) {
            case 0:  // Bala a la izquierda
                direction = angle - spreadAngle; // Dispara hacia la izquierda
                break;
            case 1:  // Bala central
                direction = angle; // Dispara directamente hacia abajo
                break;
            case 2:  // Bala a la derecha
                direction = angle + spreadAngle; // Dispara hacia la derecha
                break;
            default: // Reiniciar el índice
                currentBullet = 0;
                direction = angle;
                break;
        }

        // Calcular la velocidad de la bala
        float bulletVelocityX = (float) Math.cos(direction) * speed;
        float bulletVelocityY = (float) Math.sin(direction) * speed;

        // Configurar la bala
        bullet.setVelocityX(bulletVelocityX);
        bullet.setVelocityY(bulletVelocityY);

        // Incrementar el índice para la siguiente bala
        currentBullet++;
        if (currentBullet >= cantBullet) {
            currentBullet = 0; // Reinicia para el próximo ciclo
        }
        //System.out.println("CurrBullet = "+currentBullet);
    }
}