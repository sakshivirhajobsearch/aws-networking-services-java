package com.aws.networking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import software.amazon.awssdk.services.directconnect.DirectConnectClient;
import software.amazon.awssdk.services.directconnect.model.Connection;
import software.amazon.awssdk.services.directconnect.model.ConnectionState;
import software.amazon.awssdk.services.directconnect.model.DescribeConnectionsRequest;
import software.amazon.awssdk.services.directconnect.model.DescribeConnectionsResponse;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ec2.model.VpcState;

public class AwsNetworkingServiceDemoTest {

	private Ec2Client mockEc2Client;
	private DirectConnectClient mockDcClient;

	@BeforeEach
	void setup() {
		mockEc2Client = Mockito.mock(Ec2Client.class);
		mockDcClient = Mockito.mock(DirectConnectClient.class);
	}

	@Test
	void testDescribeVpcs() {
		Vpc vpc = Vpc.builder().vpcId("vpc-123456").cidrBlock("10.0.0.0/16").state(VpcState.AVAILABLE).build();

		DescribeVpcsResponse response = DescribeVpcsResponse.builder().vpcs(List.of(vpc)).build();

		when(mockEc2Client.describeVpcs(any(DescribeVpcsRequest.class))).thenReturn(response);

		DescribeVpcsResponse actualResponse = mockEc2Client.describeVpcs(DescribeVpcsRequest.builder().build());

		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.vpcs().size());
		assertEquals("vpc-123456", actualResponse.vpcs().get(0).vpcId());
	}

	@Test
	void testDescribeDirectConnectConnections() {
		Connection connection = Connection.builder().connectionId("dxcon-abc123").location("EqDC2").bandwidth("1Gbps")
				.connectionState(ConnectionState.AVAILABLE).build();

		DescribeConnectionsResponse response = DescribeConnectionsResponse.builder().connections(List.of(connection))
				.build();

		when(mockDcClient.describeConnections(any(DescribeConnectionsRequest.class))).thenReturn(response);

		DescribeConnectionsResponse actualResponse = mockDcClient
				.describeConnections(DescribeConnectionsRequest.builder().build());

		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.connections().size());
		assertEquals("dxcon-abc123", actualResponse.connections().get(0).connectionId());
	}
}
