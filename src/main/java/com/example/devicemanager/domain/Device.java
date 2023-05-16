package com.example.devicemanager.domain;

import com.example.devicemanager.common.DeviceStatus;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "device_tb")
public class Device {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "device_name", length = 50)
    private String name;

    @Enumerated(value = STRING)
    private DeviceStatus status;

    @Version
    private Integer version;
}
