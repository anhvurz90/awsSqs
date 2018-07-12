Amazon Simple Queue Service
01.What is Amazon SQS: {
	- What are the main Benefits of Amazon SQS? {
		+ Security, Durability, Availability, 
		   Scalability, Reliability, Customization
	}
	-  How is Amazon SQS Different from Amazon MQ or Amazon SNS?
	- What Type of Queue do I need?: {
		+ Standard, FIFO
	}
	- How can I get started with Amazon SQS?
}
02.New and Frequently Viewed Topics: {
	- Amazon SQS Developer Guide
	- Amazon SQS API Reference
}
03.Setting Up Amazon SQS: {
	03.01.Step1: Create an AWS Account:
		https://aws.amazon.com/
	03.02.Step2: Create an IAM User:
		https://console.aws.amazon.com/iam/.
	03.03.Step3: Get Your Access Key ID and Secret Access Key:
		https://console.aws.amazon.com/iam/home?#home
	03.04.Step4: Get Ready to Use the Example Code:
		Using AWS Management Console and Java.
}
04.Getting Started with Amazon SQS: {
	04.01.Step1: Create a Queue:
		https://console.aws.amazon.com/sqs/
	04.02.Step2: Send a Message:
		Choose queue, then "Send a Message"
	04.03.Receive and Delete your Message:
		Choose queue, then "View/Delete Messages"
	04.04.Delete your Queue:
		Choose queue, then "Delete Queue"
}
05.Tutorials: {
	05.01.Creating Queues: {
		05.01.01.Creating a Queue: {
			AWS SDK for Java: {
				- Setup AWS Credentials:
					+ https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
				- Code: {
					// Create a queue 
					{
						System.out.println("Creating a new SQS queue called MyQueue.\n");
						final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
						final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
					}
					
					// Create a FIFO queue
					{
						System.out.println("Creating a new Amazon SQS FIFO queue called " + "MyFifoQueue.fifo.\n");
						final Map<String, String> attributes = new HashMap<String, String>();

						// A FIFO queue must have the FifoQueue attribute set to True
						attributes.put("FifoQueue", "true");

						// If the user doesn't provide a MessageDeduplicationId, generate a MessageDeduplicationId based on the content.
						attributes.put("ContentBasedDeduplication", "true");

						// The FIFO queue name must end with the .fifo suffix
						final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyFifoQueue.fifo")
							.withAttributes(attributes);
						final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
					}
				}
				
			}
		}
		05.01.02.Creating a Queue with SSE: {
			SSE: Server-Side Encryption
			Enable SSE for a queue to protect its data.
			AWS SDK for Java: {
				- Setup AWS Credentials:
					+ https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
				- Creates a new queue with SSE using the AWS managed CMK for Amazon SQS: {
					final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
					final CreateQueueRequest createRequest = new CreateQueueRequest("MyQueue");
					final Map<String, String> attributes = new HashMap<String, String>();
					 
					// Enable server-side encryption by specifying the alias ARN of the
					// AWS managed CMK for Amazon SQS.
					final String kmsMasterKeyAlias = "arn:aws:kms:us-east-2:123456789012:alias/aws/sqs";
					attributes.put("KmsMasterKeyId", kmsMasterKeyAlias);
					 
					// (Optional) Specify the length of time, in seconds, for which Amazon SQS can reuse 
					attributes.put("KmsDataKeyReusePeriodSeconds", "60");

					final CreateQueueResult createResult = client.createQueue(createRequest);
				}
				- Creates a new queue with SSE using a custom CMK: {
					final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
					final CreateQueueRequest createRequest = new CreateQueueRequest("MyQueue");
					final Map<String, String> attributes = new HashMap<String, String>();
					 
					// Enable server-side encryption by specifying the alias ARN of the custom CMK.
					final String kmsMasterKeyAlias = "arn:aws:kms:us-east-2:123456789012:alias/MyAlias";
					attributes.put("KmsMasterKeyId", kmsMasterKeyAlias);
					 
					// (Optional) Specify the length of time, in seconds, for which Amazon SQS can reuse 
					// a data key to encrypt or decrypt messages before calling AWS KMS again.
					attributes.put("KmsDataKeyReusePeriodSeconds", "864000");
					 
					final CreateQueueResult createResult = client.createQueue(createRequest);
				}
			}
		}
	}
	05.02.Listing All Queues: {
		System.out.println("Listing all queues in your account.\n");
		for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
		    System.out.println("  QueueUrl: " + queueUrl);
		}
		System.out.println();
	}
	05.03.Adding Permissions to a Queue: {
		Using AWS Management Console
	}
	05.04.Adding, Updating, and Removing Tags for a Queue: {
		- List the tags added to a queue: {
			final ListQueueTagsRequest listQueueTagsRequest = new ListQueueTagsRequest(queueUrl);
			final ListQueueTagsResult listQueueTagsResult = SQSClientFactory.newSQSClient()
				.listQueueTags(listQueueTagsRequest);
			System.out.println(String.format("ListQueueTags: \tTags for queue %s are %s.\n", 
				QUEUE_NAME, listQueueTagsResult.getTags()));
		}
		- Add or update the values of the queue's tags using the tag's key: {
			final Map<String, String> addedTags = new HashMap<>();
			addedTags.put("Team", "Development");
			addedTags.put("Priority", "Beta");
			addedTags.put("Accounting ID", "456def");
			final TagQueueRequest tagQueueRequest = new TagQueueRequest(queueUrl, addedTags);

			System.out.println(String.format("TagQueue: \t\tAdd tags %s to queue %s.\n", addedTags, QUEUE_NAME));
			SQSClientFactory.newSQSClient().tagQueue(tagQueueRequest);
		}
		- Remove a tag from the queue using the tag key: {
			final List<String> tagKeys = Arrays.asList("Accounting ID");
			final UntagQueueRequest untagQueueRequest = new UntagQueueRequest(queueUrl, tagKeys);
			System.out.println(String.format("UntagQueue: \tRemove tags %s from queue %s.\n", tagKeys, QUEUE_NAME));
			SQSClientFactory.newSQSClient().untagQueue(untagQueueRequest);
		}
	}
	05.05.Sending messages: {
		05.05.01.Sending a message: {
			AWS SDK for Java: {
				// Send a message
				System.out.println("Sending a message to MyQueue.\n");
				sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));
			}
		}
		05.05.02.Sending a message with Attributes: {
			final Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
			messageAttributes.put("Name", new MessageAttributeValue()
				.withDataType("String")
				.withStringValue("Jane"));
			
			// Send a message with an attribute
			final SendMessageRequest sendMessageRequest = new SendMessageRequest();
			sendMessageRequest.withMessageBody("This is my message text.");
			sendMessageRequest.withQueueUrl(myQueueUrl);
			sendMessageRequest.withMessageAttributes(messageAttributes);
			sqs.sendMessage(sendMessageRequest);
		}
		05.05.03.Sending a message with a Timer: {
			- throws InterruptedException
			
			// Send a message with a 5-second timer.
			System.out.println("Sending a message with a 5-second timer to MyQueue.\n");
			SendMessageRequest request = new SendMessageRequest(myQueueUrl, "This is my message text.");
			request.setDelaySeconds(5);
			sqs.sendMessage(request);

			// Wait for 10 seconds.
			System.out.println("Waiting for 10 seconds.");
			Thread.sleep(10000L);
		}
	}
}