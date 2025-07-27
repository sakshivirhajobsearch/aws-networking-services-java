package com.aws.networking.services;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import software.amazon.awssdk.services.directconnect.DirectConnectClient;
import software.amazon.awssdk.services.directconnect.model.*;

public class AwsNetworkingServiceDemo {

	public static void main(String[] args) {
		
		Region region = Region.US_EAST_1;

		// Amazon VPC: Describe VPCs
		try (Ec2Client ec2 = Ec2Client.builder().region(region).build()) {
			describeVpcs(ec2);
		}

		// AWS Direct Connect: Describe Connections
		try (DirectConnectClient dcClient = DirectConnectClient.builder().region(region).build()) {
			describeDirectConnectConnections(dcClient);
		}
	}

	private static void describeVpcs(Ec2Client ec2) {
		System.out.println("Fetching VPCs in your AWS account...");

		DescribeVpcsRequest vpcsRequest = DescribeVpcsRequest.builder().build();
		DescribeVpcsResponse response = ec2.describeVpcs(vpcsRequest);

		for (Vpc vpc : response.vpcs()) {
			System.out.printf("VPC ID: %s, CIDR: %s, State: %s%n", vpc.vpcId(), vpc.cidrBlock(), vpc.state());
		}
	}

	private static void describeDirectConnectConnections(DirectConnectClient dcClient) {
		System.out.println("\nFetching AWS Direct Connect connections...");

		DescribeConnectionsRequest request = DescribeConnectionsRequest.builder().build();
		DescribeConnectionsResponse response = dcClient.describeConnections(request);

		for (Connection connection : response.connections()) {
			System.out.printf("Connection ID: %s, Location: %s, Bandwidth: %s, State: %s%n", connection.connectionId(),
					connection.location(), connection.bandwidth(), connection.connectionStateAsString());
		}
	}
}